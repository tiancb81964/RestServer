package com.asiainfo.ocmanager.rest.resource.quotaUtils;

/**
 * Created by zhangfq on 2017/6/27.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.asiainfo.ocmanager.persistence.model.Quota;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;

public class HbaseUtil {

    public static final Configuration conf = new Configuration();
    private static Properties prop = new Properties();

    private static Logger logger = Logger.getLogger(HbaseUtil.class);

     static {
        String classPath = new AmbariUtil().getClass().getResource("/").getPath();
        String currentClassesPath = classPath.substring(0, classPath.length() - 8)+ "conf/config.properties";
        try{
            InputStream inStream = new FileInputStream(new File(currentClassesPath ));
            prop.load(inStream);
        }catch(IOException e){
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取hbase资源用量信息
     * @param namespace
     * @return hbaseQuota
     */
    public static List<Quota> getHbaseData(String namespace){

        int tabnum = 0;
        int regnum = 0;
        List<Quota> result = new ArrayList<Quota>();
        //进行kerberos认证
        String currentClassPath = new HbaseUtil().getClass().getResource("/").getPath();
        String  keytabPath= currentClassPath.substring(0, currentClassPath.length() - 8) +"conf/"+prop.getProperty("hbase.kerberos.keytab.name");
        String  krbPath = currentClassPath.substring(0,currentClassPath.length() - 8) + "conf/"+prop.getProperty("kerberos.krb.name");
        String hbaseurl = AmbariUtil.getUrl("hbase");
        conf.set("hbase.zookeeper.quorum", hbaseurl);
        conf.set("hbase.zookeeper.property.clientPort", prop.getProperty("hbase.clientport"));
        conf.set("zookeeper.znode.parent", prop.getProperty("zookeeper.znode.parent"));

        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hbase.security.authentication", "kerberos");
        conf.set("hbase.master.kerberos.principal", prop.getProperty("hbase.master.kerberos.principal"));
        conf.set("hbase.regionserver.kerberos.principal", prop.getProperty("hbase.regionserver.kerberos.principal"));
        System.setProperty("java.security.krb5.conf",krbPath);
        UserGroupInformation.setConfiguration(conf);

        try {
            UserGroupInformation.loginUserFromKeytab(prop.getProperty("hbase.kerberos.principal"), keytabPath);
            Connection hconn = ConnectionFactory.createConnection(conf);
            Admin admin = hconn.getAdmin();
            //获取资源信息
            TableName[] tables = admin.listTableNamesByNamespace(namespace);
            tabnum = tables.length;
            for(TableName tab:tables){
                if(tab!=null){
                    List<HRegionInfo> tableRegions = admin.getTableRegions(tab);
                    int regs = tableRegions.size();
                    regnum = regnum+regs;
                }
            }
            logger.info("tabnum:"+tabnum+"------  regnum:"+regnum);
            //将获取的信息封装
            Quota tabquota = new Quota();
            Quota regquota = new Quota();
            tabquota.setName("maximumTablesQuota");
            tabquota.setUsed(String.valueOf(tabnum));
            regquota.setName("maximumRegionsQuota");
            regquota.setUsed(String.valueOf(regnum));
            result.add(tabquota);
            result.add(regquota);
        } catch (IOException e) {
            logger.error(e.getMessage());
            Quota tabquota = new Quota();
            Quota regquota = new Quota();
            tabquota.setName("maximumTablesQuota");
            tabquota.setUsed(String.valueOf(-1));
            regquota.setName("maximumRegionsQuota");
            regquota.setUsed(String.valueOf(-1));
            result.add(tabquota);
            result.add(regquota);
        }
        return result;
    }
}
