package com.alex.db.config;

import com.alex.db.configInterface.IBaseDBConfig;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class DataSourceConfig {

    public DataSource mainDataSource(IBaseDBConfig config) {
        //引用參數建立datasource
        DataSource build = DataSourceBuilder.create().username(config.getUsername()).password(config.getPazzd())
                .url(config.getUrl()).driverClassName(config.getDriverClassName()).build();
        return build;
    }

    public PlatformTransactionManager staticDBManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
