package com.asiainfo.ocmanager.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * New server configuration
 * 
 * @author Ethan
 *
 */
public class ClustersIni {
	private static final Logger LOG = LoggerFactory.getLogger(ClustersIni.class);
	private static String PATH;
	private Map<String, ClusterConfig> conf;
	private static ClustersIni instance;

	static {
		try {
			String base = ClustersIni.class.getResource("/").getPath() + ".." + File.separator;
			PATH = base + "conf" + File.separator + "clusters.ini";
			if (!new File(PATH).exists()) {
				LOG.error("File not found: " + PATH);
				throw new RuntimeException("File not found: " + PATH);
			}
		} catch (Throwable e) {
			LOG.error("Exception while init class: ", e);
			throw new RuntimeException("Exception while init class: ", e);
		}
	}
	
	/**
	 * Get clusters
	 * @return
	 */
	public Map<String, ClusterConfig> getClusters(){
		return conf;
	}

	public static ClustersIni getInstance() {
		if (instance == null) {
			synchronized (ClustersIni.class) {
				if (instance == null) {
					instance = new ClustersIni();
				}
			}
		}
		return instance;
	}

	private ClustersIni() {
		init();
	}

	private void init() {
		conf = Maps.newHashMap();
		try {
			Wini file = new Wini(new File(PATH));
			for (Entry<String, Section> en : file.entrySet()) {
				conf.put(en.getKey(), new ClusterConfig(en.getKey(), en.getValue()));
			}
		} catch (InvalidFileFormatException e) {
			LOG.error("Illegal format in file: " + PATH, e);
			throw new RuntimeException("Illegal format in file: " + PATH, e);
		} catch (IOException e) {
			LOG.error("Error while parsing config file: " + PATH, e);
			throw new RuntimeException("Error while parsing config file: " + PATH, e);
		}
	}

	public static void main(String[] args) throws InvalidFileFormatException, IOException {
		System.out
				.println("end of mian: " + ClustersIni.getInstance().getCluster("ochadoop_mycluster").getProperties());
	}

	/**
	 * Get cluster of specified name
	 * 
	 * @param section
	 * @return
	 */
	public ClusterConfig getCluster(String clustername) {
		ClusterConfig cluster = conf.get(clustername);
		return cluster;
	}

}
