# RestServer Readme
## Pre-request
1. Runtime JRE1.8 +  
2. OCDP2.6 +  
3. Network is reachable between RestServer and OCDP cluster, and other service servers like Mysql, Mongo, Greenplum etc.  
4. Kafka JMX enabled(Requested by quota monitor, enable by appending below line to Kafka->Configuration->Advanced kafka-env->'kafka-env template'):  
**export JMX_PORT=9999**  

   Skip to next step if Kerberos disabled. **If Kerberos enabled**, change Kafka configuration by folowing steps(Due to Ambari bug):  
   **a)** Open and login OCDP web page: *http://ambari_server_ip:8080*  
   **b)**. Find Kafka service on left edge, click it and then click Configuration tab on the right page.  
   **c)**. Find **listeners** configuration in '*Kafka Broker*' section and append **SASL_PLAINTEXT://localhost:kafkaport** to the end with delimiter of comma  
   **d)**. Find **security.inter.broker.protocol** configuration in '*Custom kafka-broker*' section and change value to **SASL_PLAINTEXT**  

   Complete above changes and restart Kafka cluster.  
   *Attention: Above configuration changes must be made due to Ambari bug('PLAINTEXTSASL' protocol is no longer recognized by Kafka above 0.9.0 version). It wouldn't be neccessary to follow current step is Ambari fix this in future release*  

5. Should install the Kerberos cleint. For example(Assume you already have the yum repos):
```
yum install krb5-devel krb5-workstation krb5-libs
```
6. Need OCDF installed, the OCManager need OCDF services.

## How to deploy the OCManager REST API into the tomcat

1. Clone the code form the git
```
git clone git@github.com:OCManager/RestServer.git
```

2. Install the Maven access the link: __http://maven.apache.org/download.cgi__ to get the Maven 


3. Build the RestServer, go to the RestServer folder run
(__note:__ should configure the environemnt var)
```
mvn install
```
After build successlly, it will have a __ocmanager.war__ under the __target__ folder


4. Install the tomcat into your dev environemnt

5. Install the mysql dadtabase on your environment 

6. Copy the __ocmanager.war__ folders into the __<TOMCAT_HOME>/webapps__
```
cp ocmanager.war <TOMCAT_HOME>/webapps
```

7. Start tomcat
```
<TOMCAT_HOME>/bin/startup.sh
```

8. Connect to the mysql, then run the comanager  initOCManager.sql
```
mysql -u user -p password
mysql> source <TOMCAT_HOME>/webapps/ocmanager/WEB-INF/database/mysql/initOCManager.sql
```

9. Configure RestServer configuration, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__, edit file __server.properties__
```

#Either 'ldap' or 'mysql' is supposed, to specifying user source	
oc.server.user.source=mysql

#ocdp-based services in lowercase
#Notice:service names(which in square brackets) should be aligned with the service names from catalog
oc.ocdp.services=hdfs_cluster1,hdfs_cluster2,hbase_cluster1,hbase_cluster2,hive_cluster1,mapreduce_cluster1,spark_cluster1,kafka_cluster1

#listeners(seperated by comma) that will listen on exceeded-lifetime-tenant event
oc.tenant.lifetime.manager.listeners=com.asiainfo.ocmanager.tenant.management.listener.DefaultListener,com.asiainfo.ocmanager.tenant.management.listener.EmailNotifyListener

#for ocsp, need remove from current file in future
oc.kafka.serviceName=ocdp

```

10. Configure the services that's been put on to RestServer, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __services.ini__.  
__Notice:__ service names(which in square brackets) should be aligned with the service names from catalog
```
[hdfs_cluster1]
client.class=com.asiainfo.ocmanager.service.client.v2.HDFSClient
#SimpleAuthenticator for non-secure service, KerberosAuthenticator for secure service
auth.class=com.asiainfo.ocmanager.security.KerberosAuthenticator
auth.principal=nn/aicloud1.asiainfo.com@ASIAINFO.COM
auth.keytab=E:\\kerberos\\nn.service.keytab
fs.defaultFS = 10.1.236.116:8020
ipc.client.fallback-to-simple-auth-allowed=true

[hdfs_cluster2]
client.class=com.asiainfo.ocmanager.service.client.v2.HDFSClient
#SimpleAuthenticator for non-secure service, KerberosAuthenticator for secure service
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
fs.defaultFS = 10.1.236.61:8020
ipc.client.fallback-to-simple-auth-allowed=true

[hbase_cluster1]
client.class=com.asiainfo.ocmanager.service.client.v2.HbaseClient
#SimpleAuthenticator for non-secure service, KerberosAuthenticator for secure service
auth.class=com.asiainfo.ocmanager.security.KerberosAuthenticator
auth.principal=nn/aicloud1.asiainfo.com@ASIAINFO.COM
auth.keytab=E:\\kerberos\\nn.service.keytab
hbase.zookeeper.quorum=aicloud1.asiainfo.com,aicloud2.asiainfo.com
hbase.zookeeper.property.clientPort=2181
hbase.master.kerberos.principal=hbase/_HOST@ASIAINFO.COM
hbase.regionserver.kerberos.principal=hbase/_HOST@ASIAINFO.COM
#SIMPLE for non-secure hbase, KERBEROS for secure hbase
hbase.security.authentication=KERBEROS
#/hbase-unsecure for non-secure hbase, /hbase-secure for secure hbase
zookeeper.znode.parent=/hbase-secure

[hbase_cluster2]
client.class=com.asiainfo.ocmanager.service.client.v2.HbaseClient
#SimpleAuthenticator for non-secure service, KerberosAuthenticator for secure service
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
hbase.zookeeper.quorum=ochadoop111.jcloud.local,ochadoop112.jcloud.local,xiaomm.jcloud.local
hbase.zookeeper.property.clientPort=2181
zookeeper.znode.parent=/hbase-unsecure

[yarn_cluster1]
client.class=com.asiainfo.ocmanager.service.client.v2.YarnClient
#Yarn client is implemented by HTTP, set to SimpleAthenticator 
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
oc.yarn.resourcemanager.http.url=http://aicloud1.asiainfo.com:8088,http://aicloud2.asiainfo.com:8088

[yarn_cluster2]
client.class=com.asiainfo.ocmanager.service.client.v2.YarnClient
#Yarn client is implemented by HTTP, set to SimpleAthenticator 
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
oc.yarn.resourcemanager.http.url=http://xiaomm.jcloud.local:8088

[hive_cluster1]
client.class=com.asiainfo.ocmanager.service.client.v2.HiveClient
#SimpleAuthenticator for non-secure service, KerberosAuthenticator for secure service
auth.class=com.asiainfo.ocmanager.security.KerberosAuthenticator
auth.principal=nn/aicloud1.asiainfo.com@ASIAINFO.COM
auth.keytab=E:\\kerberos\\nn.service.keytab
fs.defaultFS = 10.1.236.116:8020
ipc.client.fallback-to-simple-auth-allowed=true

[kafka_cluster1]
client.class=com.asiainfo.ocmanager.service.client.v2.KafkaClient
#Kafka client is implements by PLAINTEXT protocal, set to SimpleAthenticator
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
oc.zookeeper.quorum=aicloud1.asiainfo.com,aicloud2.asiainfo.com
oc.zookeeper.port=2181

[kafka_cluster2]
client.class=com.asiainfo.ocmanager.service.client.v2.KafkaClient
#Kafka client is implements by PLAINTEXT protocal, set to SimpleAthenticator
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
oc.zookeeper.quorum=ochadoop111.jcloud.local,ochadoop112.jcloud.local,xiaomm.jcloud.local
oc.zookeeper.port=2181

[mongo]
client.class=com.asiainfo.ocmanager.service.client.v2.MongoDBClient
auth.class=com.asiainfo.ocmanager.security.SimpleAthenticator
oc.mongo.server.host=ochadoop111.jcloud.local
oc.mongo.server.port=27021
```

11. Configure the clusters properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __clusters.ini__
```
[mycluster_alpha]
oc.ambari.hostname=10.1.236.116
oc.ambari.port=8080
oc.ambari.protocol=http
oc.ambari.username=admin
oc.ambari.password=admin
oc.hdp.version=2.6.0.3-8
oc.ranger.hosts=10.1.236.116
oc.ranger.port=6080
oc.ranger.admin=admin
oc.ranger.admin.password=admin

[mycluster_beta]
oc.ambari.hostname=10.1.236.61
oc.ambari.port=8080
oc.ambari.protocol=http
oc.ambari.username=admin
oc.ambari.password=admin
oc.hdp.version=2.6.0.3-8
oc.ranger.hosts=ochadoop111.jcloud.local
oc.ranger.port=6080
oc.ranger.admin=admin
oc.ranger.admin.password=admin
```

12. Configure Mail-Server properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __mailserver.properties__
```
#mail server ip
mail.smtp.host=mail.asiainfo.com
#mail server port
mail.smtp.port=25
mail.debug=false
#mail server auth enabled
mail.smtp.auth=true
#mail account to be logged in
ocmail.account=admin@asiainfo.com
#mail account password
ocmail.password=123456
```

13. Configure the database properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __mysql.properties__
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.encoding=useUnicode=true&characterEncoding=utf8
jdbc.url=jdbc:mysql://<mysql server ip>:3306/ocmanager
jdbc.username=<the user create the ocmanager scheame>
jdbc.password=<the user password create the ocmanager scheame>
```

14. Configure the df properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __dataFoundry.properties__
```
dataFoundry.url=https://<df rest server IP>:<df rest server api port>
dataFoundry.token=<df admin token>
```

15. Configure Ldap properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__, edit file __ldap.properties__
```
#Ldap server URL, in format 'ldap://ldap_server_host:port'
ldap.url=ldap://10.1.236.146:389
#Ldap base entry, from where to search for users
ldap.base.name=ou=People,dc=asiainfo,dc=com
#Ldap search filter in RFC2254
ldap.search.filter=objectClass=person
```
go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__, edit file __shiroLdap.ini__
```
#Ldap realm
ldapRealm = org.apache.shiro.realm.ldap.JndiLdapRealm
#user template to use for authentication
ldapRealm.userDnTemplate = uid={0},ou=People,dc=asiainfo,dc=com
#Ldap server URL, in format 'ldap://ldap_server_host:port'
ldapRealm.contextFactory.url = ldap://10.1.236.146:389
#Ldap realm to use for Shiro authentication
securityManager.realms = $ldapRealm
```

16. Configure Kerberos properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__, edit file __kerberos.properties__ (__NOTE: If you did NOT enable Kerberos we can ignore this step__)
```
kerberos.user.principal=admin/admin@ASIAINFO.COM
kerberos.keytab.location=/etc/krb5.conf
kerberos.admin.password=00AAaa00
kerberos.kdc.host=10.1.236.146
kerberos.realm=ASIAINFO.COM
```

17. If you need to run build-in hadoop "Hello World" examples(eg. Mapreduce Pi / SparkPi), you have to be granted the 'rwx' privileges of the Jar path on HDFS. We've provided the automatic shell script to do the job for you. Find file __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/scripts/create-common-policy.sh__, run script:
```
sh create-common-policy.sh clusterName rangerIP rangerUser rangerPassword
example:
sh create-common-policy.sh mycluster 127.0.0.1 admin admin
```

18. To do management of services that's supported by RestServer, you need to make sure the user running RestServer has full privileges over those services. Append the user to ranger policies mannually through Web: __http://ambari_server_host:6080/login.jsp__		
__Attention: user should be appended to the first policy under all services__.

19. Restart the tomcat server

20. Try access __http://<your tomcat server>:<port>/ocmanager/v1/api/tenant__, you get response from server if all above steps been done correctly


__NOTE: More rest api, please access the link:__  https://github.com/OCManager/RestServer/tree/master/docs/adaptorRest