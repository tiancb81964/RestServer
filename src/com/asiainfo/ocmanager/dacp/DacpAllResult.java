package com.asiainfo.ocmanager.dacp;

import com.asiainfo.ocmanager.dacp.model.*;
import com.asiainfo.ocmanager.dacp.service.TeamWrapper;
import com.asiainfo.ocmanager.dacp.service.UserWrapper;
import com.asiainfo.ocmanager.dacp.utils.Property;
import com.asiainfo.ocmanager.rest.resource.quotaUtils.restClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by zhangfq on 2017/7/5.
 */
public class DacpAllResult {

//    private static Map info;
//    private static Team team;
//    private static String result;
    private static Properties prop;

    private static Logger logger = Logger.getLogger(DacpAllResult.class);

    /**
     * 进行数据整合
     * @param tenantId
     * @return
     */
    public static String getAllResult(String tenantId){
        logger.info("begin to call dacp interface");

        String classPath = new Property().getClass().getResource("/").getPath();
        String currentClassesPath = classPath.substring(0, classPath.length() - 8)+ "conf/config.properties";
        try{
            InputStream inStream = new FileInputStream(new File(currentClassesPath ));
            prop = new Properties();
            prop.load(inStream);
        }catch(IOException e){
            logger.error(e.getMessage());
        }


        Map info = new HashMap<>();
        String userInfo = null;
        try{
            //获取租户信息
            Team team = TeamWrapper.getTeamFromTenant(tenantId);
            List<Team> teams = new ArrayList<>() ;
            teams.add(team);
            //获取用户信息
            List<UserInfo> userInfos = UserWrapper.getUserInfoFromUserRoleView(tenantId);
            //获取数据库实例创建，分配信息
            Map<String,List> map = DacpForResourceUtil.getDatabaseData(tenantId);
            List<DBRegister> DBRegisters = map.get("database");
            List<DBDistribution> DBDistributions = map.get("transdatabase");
            // 将数据转成json格式
            GsonBuilder gb =new GsonBuilder();
            gb.disableHtmlEscaping();
            String teamStr = gb.create().toJson(teams);
            String userInfoStr = gb.create().toJson(userInfos);
            String DBRegisterStr = gb.create().toJson(DBRegisters);
            String DBDistributionStr = gb.create().toJson(DBDistributions);
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userinfo",userInfoStr);
            jsonObject.addProperty("team",teamStr);
            jsonObject.addProperty("database",DBRegisterStr);
            jsonObject.addProperty("transdatabase",DBDistributionStr);

            String infoStr = gb.create().toJson(jsonObject);

            String returnInfo = infoStr.replace("\\","").replace("\"[","[").replace("]\"","]");
            logger.info(returnInfo);
            info.put("info",returnInfo);
            logger.info("dacp url :"+prop.getProperty("dacp.url"));
            String restResult = restClient.post(prop.getProperty("dacp.url"),info);
            DacpResult dacpResult = gson.fromJson(restResult,DacpResult.class);
            userInfo = dacpResult.getResult();
            //日志记录结果
            if(userInfo.equals("true")){
                logger.info("dacp is ok");
            }else{
                logger.error("sync dacp is failed,error message is:" + dacpResult.getMessage());
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.info("getAllResult exception"+e.getMessage());
        }
        logger.info("end to call dacp interface");
        return userInfo;

    }
}
