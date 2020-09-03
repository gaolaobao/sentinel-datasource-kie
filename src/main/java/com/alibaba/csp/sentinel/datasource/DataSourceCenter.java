package com.alibaba.csp.sentinel.datasource;

import com.alibaba.csp.sentinel.datasource.kie.common.ServiceInfo;
import com.alibaba.csp.sentinel.datasource.rule.RuleCenter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DataSourceCenter {
    private static final String DEFAULT_RULE_FILE = "default-rules.json";

    @Autowired
    ServiceInfo serviceInfo;

    Map<String, AbstractDataSource<?, ?>> dataSourceMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){

    }

    public void getDefaultRules(){
        URL resource = getClass().getClassLoader().getResource(DEFAULT_RULE_FILE);
        if (resource == null || resource.getFile() == null){
            log.warn(String.format("Default rule file (%s) is not exist.", DEFAULT_RULE_FILE));
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        try(FileInputStream inputStream = new FileInputStream(resource.getFile())){
            int buf;
            while((buf = inputStream.read()) != -1){
                stringBuilder.append((char)buf);
            }
        }catch (IOException e){
            log.error(String.format("Get default rule file (%s) failed.", DEFAULT_RULE_FILE), e);
            return;
        }

        JSONObject jsonObject = JSON.parseObject(stringBuilder.toString());

        for(Map.Entry<String, Object> entry : jsonObject.entrySet()){
            String ruleKey = entry.getKey();
            String ruleValue = entry.getValue().toString();
            RuleCenter.registerRuleManager(ruleKey, ruleValue);
        }
    }
}
