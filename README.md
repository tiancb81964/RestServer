# RestServer Readme


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


9. Congifure the database properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit the mysql.properties
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.encoding=useUnicode=true&characterEncoding=utf8
jdbc.url=jdbc:mysql://<mysql server ip>:3306/ocmanager
jdbc.username=<the user create the ocmanager scheame>
jdbc.password=<the user password create the ocmanager scheame>
```

10. Congifure the df properties, go to __<TOMCAT_HOME>/webapps/ocmanager/WEB-INF/conf__ , edit the dataFoundry.properties
```
dataFoundry.url=https://<df rest server IP>:<df rest server api port>
dataFoundry.token=<df admin token>
```

11. Then restart the tomcat server

12. Then Access __http://<your tomcat server>:<port>/ocmanager/v1/api/tenant__ you can see the data


__NOTE: __ More rest api, please access the link: https://github.com/OCManager/RestServer/tree/master/docs/adaptorRest