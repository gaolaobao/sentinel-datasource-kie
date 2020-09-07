package com.alibaba.csp.sentinel.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataSourceCenter {
    @Getter
    private static final Map<String, DataSourceWrapper> dataSourcesMap = new ConcurrentHashMap<>();

    @Value("${sentinel.source.type}")
    private String dataSourceType;

    @PostConstruct
    public void init(){
        DataSourceWrapper sourceWrapper = dataSourcesMap.get(dataSourceType);

        sourceWrapper.init();
    }

    public static void register(String sourceType, DataSourceWrapper dataSourceWrapper){
        dataSourcesMap.put(sourceType, dataSourceWrapper);
    }
}
