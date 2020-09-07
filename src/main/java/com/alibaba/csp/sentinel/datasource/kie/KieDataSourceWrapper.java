package com.alibaba.csp.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.DataSourceWrapper;
import com.alibaba.csp.sentinel.datasource.kie.common.ServiceInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class KieDataSourceWrapper extends DataSourceWrapper {
    private static final String DEFAULT_RULE_FILE = "default-rules.json";

    private final Map<String, String> rulesMap = new ConcurrentHashMap<>();

    private final Map<String, KieDataSource<?>> sourceMap = new ConcurrentHashMap<>();

    @Autowired
    ServiceInfo serviceInfo;

    @PostConstruct
    public void init(){
        registerDataSourceCenter();

        initDefaultRules();

        initDataSources();
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
            Optional<KieDataSource<?>> kieDataSource = getKieDataSource(entry.getKey(), entry.getValue());

            kieDataSource.ifPresent(source -> sourceMap.put(entry.getKey(), source));
        }
    }

    private Optional<KieDataSource<?>> getKieDataSource(String ruleKey, String ruleValue){
        Class<?> ruleClass;

        try {
            ruleClass = Class.forName(ruleKey);
        }catch (ClassNotFoundException e){
            log.error(String.format("No found rule: %s", ruleKey), e);
            return Optional.empty();
        }

        return getKieDataSource(ruleKey, ruleValue, ruleClass);
    }

    private <T> Optional<KieDataSource<?>> getKieDataSource(String ruleKey, String ruleValue, Class<T> ruleClass){
        KieDataSource<?> dataSource;
        Converter<String, List<T>> parser = source -> JSON.parseObject(source,
                new TypeReference<List<T>>() {});

        try {
            Constructor<?> constructor = ruleClass.getDeclaredConstructor(Converter.class, ServiceInfo.class,
                    String.class, String.class);
            dataSource = (KieDataSource<List<T>>) constructor.newInstance(parser, serviceInfo, ruleKey, ruleValue);
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            log.error(String.format("Get data source failed, rule: %s.", ruleKey), e);
            return Optional.empty();
        }

        return Optional.of(dataSource);
    }
}

