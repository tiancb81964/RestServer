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

#Either 'ldap' or 'mysql' is supposed to specify where users should be added from	
oc.server.user.source=ldap

#OCDP cluster information, it used to get the components info
oc.ambari.hostname=10.1.236.116
oc.ambari.port=8080
oc.ambari.protocol=http
oc.ambari.username=admin
oc.ambari.password=admin
oc.ambari.clustername=ochadoop_mycluster

#Ranger config
oc.ranger.hosts=aicloud1.asiainfo.com
oc.ranger.port=6080
oc.ranger.admin=admin
oc.ranger.admin.password=admin

#Period(in seconds) of authentication action	
oc.server.security.scheduler.period.seconds=85800
#If kerberos is enabled, configure to 'com.asiainfo.ocmanager.security.module.plugin.KrbModule', otherwise to 'com.asiainfo.ocmanager.security.module.plugin.SimpleModule'	
oc.server.security.mudule.class=com.asiainfo.ocmanager.security.module.plugin.KrbModule
#krb5.conf file path, needed only if Kerberos is enabled
oc.hadoop.krb.conf=/etc/krb5.conf

#For HA, configure to HDFS nameservices. For non-HA, configure to 'namenodeHostname:port'	
oc.hdfs.dfs.nameservices=aicloud1.asiainfo.com:8020
#HDFS dfs.ha.namenodes, in format like	'nn1#NN1Hostname:port,nn2#NN2Hostname:port'	
oc.hdfs.dfs.ha.namenodes=nn1#aicloud1.asiainfo.com:8020,nn2#aicloud2.asiainfo.com:8020

#Kerberos princial to use for HDFS service	
oc.hadoop.krb.pricipal=nn/aicloud1.asiainfo.com@ASIAINFO.COM
#Kerberos keytab path, must be aligned with configured principal	
oc.hadoop.krb.keytab=/etc/security/keytabs/nn.service.keytab

#Kerberos principal to use for Hbase Master, '_HOST' stands for whichever master that's active.    
oc.hbase.master.krb.principal=hbase/_HOST@ASIAINFO.COM
#Kerberos principal to use for Hbase Regionserver
oc.hbase.regionserver.krb.principal=hbase/_HOST@ASIAINFO.COM

#HTTP URL(http://host:port) of ResourceManager. Both RM can be configured using comma as seperator if HA is enabled
oc.yarn.resourcemanager.http.url=http://aicloud1.asiainfo.com:8088,http://aicloud2.asiainfo.com:8088

#Hostnames of zookeeper, seperated by comma
oc.zookeeper.quorum=aicloud1.asiainfo.com,aicloud2.asiainfo.com
#Zookeeper port
oc.zookeeper.port=2181

#Hostnames of kafka brokers, seperated by comma
oc.kafka.brokers=aicloud1.asiainfo.com,aicloud2.asiainfo.com
#Kafka port
oc.kafka.broker.port=6667
#Kafka jaas file path, needed only if Kerberos is enabled
oc.kafka.security.jaas.file=/etc/kafka/conf/kafka_jaas.conf

#Hostname of Mongo server
oc.mongo.server.host=ochadoop111.jcloud.local
#Mongo server port
oc.mongo.server.port=27021

#Hostname of Greenplum server
oc.greenplum.server.host=ochadoop111.jcloud.local
#Greenplum port
oc.greenplum.server.port=5432
#Admin account of Greenplum
oc.greenplum.user=gpadmin
#Password of admin account 
oc.greenplum.password=asiainfo
```

10. Congifure the database properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __mysql.properties__
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.encoding=useUnicode=true&characterEncoding=utf8
jdbc.url=jdbc:mysql://<mysql server ip>:3306/ocmanager
jdbc.username=<the user create the ocmanager scheame>
jdbc.password=<the user password create the ocmanager scheame>
```

11. Congifure the df properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit file __dataFoundry.properties__
```
dataFoundry.url=https://<df rest server IP>:<df rest server api port>
dataFoundry.token=<df admin token>
```

12. Configure Ldap properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__, edit file __ldap.properties__
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


13. Configure Kerberos properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__, edit file __kerberos.properties__ (__NOTE: If you did NOT enable Kerberos we can ignore this step__)
```
kerberos.user.principal=admin/admin@ASIAINFO.COM
kerberos.keytab.location=/etc/krb5.conf
kerberos.admin.password=00AAaa00
kerberos.kdc.host=10.1.236.146
kerberos.realm=ASIAINFO.COM
```

14. If you need to run build-in hadoop "Hello World" examples(eg. Mapreduce Pi / SparkPi), you have to be granted the 'rwx' privileges of the Jar path on HDFS. We've provided the automatic shell script to do the job for you. Find file __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/scripts/create-common-policy.sh__, run script:
```
sh create-common-policy.sh clusterName rangerIP rangerUser rangerPassword
example:
sh create-common-policy.sh mycluster 127.0.0.1 admin admin
```

15. To do management of services that's supported by RestServer, you need to make sure the user running RestServer has full privileges over those services. Append the user to ranger policies mannually through Web: __http://ambari_server_host:6080/login.jsp__		
__Attention: user should be appended to the first policy under all services__.

16. Restart the tomcat server

17. Try access __http://<your tomcat server>:<port>/ocmanager/v1/api/tenant__, you get response from server if all above steps been done correctly


__NOTE: More rest api, please access the link:__  https://github.com/OCManager/RestServer/tree/master/docs/adaptorRest