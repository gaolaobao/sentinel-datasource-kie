package com.alibaba.csp.sentinel.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataSourceCenter {
    @Getter
    private static final Map<String, DataSourceWrapper> dataSourcesMap = new ConcurrentHashMap<>();

    public static void register(String sourceType, DataSourceWrapper dataSourceWrapper){
        dataSourcesMap.put(sourceType, dataSourceWrapper);
    }
}
