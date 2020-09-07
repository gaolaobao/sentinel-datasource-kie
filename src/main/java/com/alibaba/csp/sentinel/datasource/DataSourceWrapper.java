package com.alibaba.csp.sentinel.datasource;

public abstract class DataSourceWrapper {
    protected String dataSourceType;

    public void registerDataSourceCenter(){
        DataSourceCenter.register(dataSourceType, this);
    }
}
