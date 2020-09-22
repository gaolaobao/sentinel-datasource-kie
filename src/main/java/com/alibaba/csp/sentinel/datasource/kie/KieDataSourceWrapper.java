package com.alibaba.csp.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.DataSourceWrapper;
import com.alibaba.csp.sentinel.datasource.kie.common.ServiceInfo;
import com.alibaba.csp.sentinel.datasource.rule.RuleCenter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component("KieDataSource")
public class KieDataSourceWrapper implements DataSourceWrapper {
    private static final String DEFAULT_RULE_FILE = "default-rules.json";

    private final Map<String, String> rulesMap = new ConcurrentHashMap<>();

    private final Map<String, KieDataSource<?>> sourceMap = new ConcurrentHashMap<>();

    @Autowired
    private RuleCenter ruleCenter;

    @Autowired
    ServiceInfo serviceInfo;

    @Override
    public void init(){
        initDefaultRules();

        initDataSources();

        registerRuleManager();
    }

    private void initDefaultRules(){
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
            rulesMap.put(entry.getKey(), entry.getValue().toString());
        }
    }

    private void initDataSources(){
        for(Map.Entry<String, String> entry : rulesMap.entrySet()){
            String ruleClassPath = entry.getKey();

            int ruleKeyIndex = ruleClassPath.lastIndexOf(".");
            String ruleKey = ruleClassPath.substring(ruleKeyIndex + 1);

            Optional<KieDataSource<?>> kieDataSource = getKieDataSource(ruleClassPath, ruleKey, entry.getValue());

            kieDataSource.ifPresent(source -> sourceMap.put(ruleKey, source));
        }
    }

    private Optional<KieDataSource<?>> getKieDataSource(String rulePath, String ruleKey, String ruleValue){
        Class<?> ruleClass;

        try {
            ruleClass = Class.forName(rulePath);
        }catch (ClassNotFoundException e){
            log.error(String.format("No found rule: %s", ruleKey), e);
            return Optional.empty();
        }

        return Optional.of(getKieDataSource(ruleKey, ruleValue, ruleClass));
    }

    private <T> KieDataSource<List<T>> getKieDataSource(String ruleKey, String ruleValue, Class<T> ruleClass){
        TypeReference<List<T>> typeReference = new TypeReference<List<T>>(ruleClass) {};
        Converter<String, List<T>> parser = source -> JSON.parseObject(source, typeReference);

        return new KieDataSource<List<T>>(parser, serviceInfo, ruleKey, ruleValue);
    }

    private void registerRuleManager(){
        for(Map.Entry<String, KieDataSource<?>> entry : sourceMap.entrySet()){
            ruleCenter.registerRuleManager(entry.getKey(), entry.getValue());
        }
    }
}

