package com.asiainfo.ocmanager.service.client.v2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.common.protocol.SecurityProtocol;
import org.apache.kafka.common.requests.MetadataResponse.TopicMetadata;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.constant.Constant;
import com.google.common.collect.Sets;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import scala.Tuple2;
import scala.collection.JavaConversions;

/**
 * Kafka client
 * @author Ethan
 *
 */
public class KafkaClient extends ServiceClient{
	private static final Logger LOG = Logger.getLogger(KafkaClient.class);
	private ZkUtils zookeeper;
	private SecurityProtocol protocol = SecurityProtocol.PLAINTEXT;

	protected KafkaClient(String serviceName, Delegator subject) {
		super(serviceName, subject);
		init();
	}
	
	/**
	 * Get used size of specified topic in Bytes.
	 * 
	 * @param topic
	 * @return
	 */
	public long fetchTopicSize(String topic) {
		Set<Partition> partitions = getLeaderPartitions(topic);
		long sizeBytes = 0l;
		for (Partition par : partitions) {
			sizeBytes = sizeBytes + partitionSize(par);
		}
		return sizeBytes;
	}
	
	private scala.collection.Set<String> toSet(String topic) {
		return JavaConversions.asScalaSet(Sets.newHashSet(topic));
	}
	
	/**
	 * Get all the leader partitions of specified topic.
	 * 
	 * @param topicName
	 * @return
	 */
	private Set<Partition> getLeaderPartitions(String topicName) {
		Set<Partition> partitions = new HashSet<>();
		scala.collection.Set<TopicMetadata> meta = AdminUtils.fetchTopicMetadataFromZk(toSet(topicName), zookeeper, protocol);
		for(org.apache.kafka.common.requests.MetadataResponse.PartitionMetadata partition : meta.head().partitionMetadata()) {
			partitions.add(new Partition(topicName, partition.partition(), partition.leader().host()));
		}
		return partitions;
	}
	
	private long partitionSize(Partition par) {
		try {
			if (par.getHost() == null || par.getHost().isEmpty()) {
				LOG.error("Leader not found for topic: " + par.getTopic() + ", partition: " + par.getPar() + ". Partition size ungettable");
				return 0l;
			}
			MBeanServerConnection conn = KafkaJMXPool.getConnection(par.getHost());
			Object value = conn.getAttribute(MBeanName.name(par.getTopic(), par.getPar()), MBeanName.VALUE);
			return (Long) value;
		} catch (Exception e) {
			LOG.error("Fetching partition size failed: " + par, e);
			throw new RuntimeException("Fetching partition size failed: " + par, e);
		}
	}

	private void init() {
		initZK();		
	}
	
	/**
	 * Get partition number of specified topic.
	 * 
	 * @param topic
	 * @return
	 */
	public int getPartitionCount(String topic) {
		scala.collection.Set<TopicMetadata> meta = AdminUtils.fetchTopicMetadataFromZk(toSet(topic), zookeeper, protocol);
		int number = meta.head().partitionMetadata().size();
		if (number <= 0) {
			LOG.error("Partition number is negtive: " + topic);
			throw new RuntimeException("Partition number is negtive: " + topic);
		}
		return number;
	}
	
	/**
	 * Get the retention size of each partition.
	 * 
	 * @return
	 */
	public long getRetensionSize(String topic) {
		Properties map = AdminUtils.fetchEntityConfig(zookeeper, "topics", topic);
		String retention = map.getProperty("retention.bytes");
		if (retention == null) {
			return -1;
		}
		return Long.valueOf(retention);
	}

	public static void main(String[] args) {
		int count = new KafkaClient("kafka", new Delegator(null)).getPartitionCount("ethantest2");
		System.out.println(">>>count: " + count);
	}
	
	private void initZK() {
		boolean isSecure = JaasUtils.isZkSecurityEnabled();
		LOG.info("Zookeeper isZkSecurityEnabled : " + isSecure);
		Tuple2<ZkClient, ZkConnection> tuple = ZkUtils.createZkClientAndConnection(assembleZKStr(), 30000, 30000);
		zookeeper = new ZkUtils(tuple._1, tuple._2, isSecure);		
	}

	private String assembleZKStr() {
		String[] zk = this.serviceConfig.getProperty(Constant.ZOOKEEPER).split(",");
		String port = this.serviceConfig.getProperty(Constant.ZOOKEEPER_PORT);
		StringBuilder sb = new StringBuilder();
		for (String item : zk) {
			sb.append(item).append(":").append(port).append(",");
		}
		String zkStr = sb.toString();
		String finalString = zkStr.substring(0, zkStr.length() - 1);
		LOG.info("Zookeeper connection string for KafkaClient: " + finalString);
		return finalString;
	}
	
	/**
	 * JMX connections cache pool for Kafka.
	 * 
	 * @author EthanWang
	 *
	 */
	private static class KafkaJMXPool {
		private static final String URL = "service:jmx:rmi:///jndi/rmi://";
		private static Map<String, JMXConnector> jmx = new ConcurrentHashMap<>();

		public static MBeanServerConnection getConnection(String host) {
			try {
				if (host == null || host.isEmpty()) {
					LOG.error("MBean connection host is null");
					throw new RuntimeException("MBean connection host can not be null.");
				}
				if (jmx.containsKey(host)) {
					JMXConnector connector = jmx.get(host);
					return validateConnection(host, connector);
				}
				cache(host, create(host));
				return jmx.get(host).getMBeanServerConnection();
			} catch (Exception e) {
				LOG.error("Error while creating KafkaJMX connection: " + host, e);
				throw new RuntimeException("Error while creating KafkaJMX connection: " + host, e);
			}
		}

		private static MBeanServerConnection validateConnection(String host, JMXConnector connector) throws IOException {
			try {
				return connector.getMBeanServerConnection();
			} catch (IOException e) {
				renew(host, create(host));
				return jmx.get(host).getMBeanServerConnection();
			}
		}
		
		private static void renew(String host, JMXConnector conn) {
			LOG.info("Renewing JMXConnection to host: " + host);
			try {
				jmx.get(host).close();
			} catch (IOException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Excepiton when closing connection: " + conn, e);
				}
			}
			jmx.remove(host);
			jmx.put(host, conn);
		}

		private static void cache(String host, JMXConnector conn) {
			jmx.put(host, conn);
		}

		private static JMXConnector create(String host) {
			try {
				JMXServiceURL url = new JMXServiceURL(URL + host + ":" + "9999/jmxrmi");
				JMXConnector jmxc = JMXConnectorFactory.connect(url);
				return jmxc;
			} catch (Exception e) {
				LOG.error("Creating KafkaJMX connection failed by host: " + host, e);
				throw new RuntimeException("Creating KafkaJMX connection failed: " + host, e);
			}
		}
	}
	
	/**
	 * MBean name constructor.
	 * 
	 * @author EthanWang
	 *
	 */
	private static class MBeanName {
		private static final String OBJECTSTR = "kafka.log:type=Log,name=Size,topic={%TOPIC},partition={%PARTITION}";
		public static final String VALUE = "Value";

		public static ObjectName name(String topic, int partition) throws MalformedObjectNameException {
			return new ObjectName(
					OBJECTSTR.replace("{%TOPIC}", topic).replace("{%PARTITION}", String.valueOf(partition)));
		}
	}
	
	/**
	 * Kafka partition, consist of parent topic, partitionid and allocated host
	 * of this partition.
	 * 
	 * @author EthanWang
	 *
	 */
	private static class Partition {
		private String topic;
		private int par; // partition number.
		private String host; // host at which partition locates.

		public Partition(String topic, int partition, String host) {
			this.par = partition;
			this.host = host;
			this.topic = topic;
		}

		public int getPar() {
			return par;
		}

		public String getTopic() {
			return topic;
		}

		public String getHost() {
			return host;
		}

		public String toString() {
			return topic + "-" + par + "@" + host;
		}

		public boolean equals(Object obj) {
			return this.toString().equals(obj.toString());
		}

		public int hashCode() {
			return -1;
		}
	}
}
