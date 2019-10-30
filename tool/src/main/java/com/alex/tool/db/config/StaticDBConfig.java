package com.alex.tool.db.config;

import com.alex.tool.db.configInterface.IBaseDBConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@PropertySource(value = { "file:${FUCO_CONFIG_PATH}/DB.properties" }, encoding = "UTF-8")
public class StaticDBConfig implements IBaseDBConfig {
    

    @Value("${staticDBConfig.username}")
    private String username;
    @Value("${staticDBConfig.password}")
    private String pazzd;
    @Value("${staticDBConfig.url}")
    private String url;
    @Value("${staticDBConfig.driverclassname}")
    private String driverclassname;
    @Value("${staticDBConfig.code}")
    private String code;
    @Value("${staticDBConfig.dbName}")
    private String dbName;

    @PostConstruct
    public void init(){
        System.out.println(getUsername());
        System.out.println(getPazzd());
    }

//    @PostConstruct
//    public void init(){
//        logger.info(updbConfig.getUrl());
//        logger.info(updbConfig.getUsername());
//        // 凱基特殊處理 要先去另一個DB 取得帳密
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setDriverClassName(updbConfig.getDriverClassName());
//        basicDataSource.setUrl(updbConfig.getUrl());
//        basicDataSource.setUsername(updbConfig.getUsername());
//        basicDataSource.setPassword(updbConfig.getPazzd());
//        logger.info("設定updb連線");
//        //DataSource dataSource = basicDataSource;
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rest = null;
//        String dbuser = "";
//        String dbpazzd = "";
//        try {
//            logger.info("開始updb連線");
//            conn = basicDataSource.getConnection();
//            logger.info("取得KPDB01的帳號密碼");
//            String sql = "exec usp_GetConnectAccountData "+code +","+username +","+dbName;
//            stmt = conn.prepareStatement(sql);
//            rest = stmt.executeQuery();
//            while(rest.next()) {
//                dbuser = rest.getString("ConnectAccount");  //1
//                dbpazzd = rest.getString("Password");  // 3
//            }
//            //logger.info("StaticDBUser:"+dbuser+" StaticDBpwd:"+dbpazzd);
//            this.setUsername(dbuser);
//            this.setPazzd(dbpazzd);
//        }catch (Exception e) {
//            logger.error("未知錯誤", e);
//        } finally {
//            try {
//                if (rest != null){
//                    rest.close();
//                }
//                if (stmt != null){
//                    stmt.close();
//                }
//                if (conn != null){
//                    conn.close();
//                    basicDataSource.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }


    
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPazzd() {
        return pazzd;
    }

    @Override
    public void setPazzd(String pazzd) {
        this.pazzd = pazzd;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getDriverClassName() {
        return driverclassname;
    }

    @Override
    public void setDriverClassName(String driverClassName) {
        this.driverclassname = driverClassName;
    }
}