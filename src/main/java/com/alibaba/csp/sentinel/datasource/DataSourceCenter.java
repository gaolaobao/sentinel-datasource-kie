package com.alibaba.csp.sentinel.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Service
public class DataSourceCenter {
    @Autowired
    private Map<String, DataSourceWrapper> dataSourcesMap;

    @Value("${sentinel.source.type}")
    private String dataSourceType;

    @PostConstruct
    public void init(){
        log.info("Begin init data sources.");

        DataSourceWrapper sourceWrapper = dataSourcesMap.get(dataSourceType);
        sourceWrapper.init();
    }
}
