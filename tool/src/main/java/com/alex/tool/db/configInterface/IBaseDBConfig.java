package com.alex.tool.db.configInterface;

/** DB連線設定的Base */
public interface IBaseDBConfig {
    public String getUsername();
    public void setUsername(String username);
    public String getPazzd();
    public void setPazzd(String pazzd);
    public String getUrl();
    public void setUrl(String url);
    public String getDriverClassName();
    public void setDriverClassName(String driverClassName);
}