package com.alex.tool.db.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private StaticDBConfig staticDBConfig;


    @Bean("mainDB")
    @Primary
    public DataSource mainDataSource() {
        //引用參數建立datasource
        DataSource build = DataSourceBuilder.create().username(staticDBConfig.getUsername()).password(staticDBConfig.getPazzd())
                .url(staticDBConfig.getUrl()).driverClassName(staticDBConfig.getDriverClassName()).build();
        return build;
    }
    @Bean(name = "staticDBManager")
    @Primary
    public PlatformTransactionManager staticDBManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
