package com.alibaba.csp.sentinel.datasource.rule;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@Getter
public class RuleCenter {
    @Autowired
    private Map<String, RuleWrapper> ruleMap;

    public void registerRuleManager(String ruleType, AbstractDataSource<String, ?> dataSource){
        if(!ruleMap.containsKey(ruleType)){
            log.error(String.format("Un support rule type %s.", ruleType));
            return;
        }

        log.info(String.format("Register %s to rule manager.", ruleType));
        RuleWrapper ruleWrapper = ruleMap.get(ruleType);
        ruleWrapper.registerRuleManager(dataSource);
    }

    public Set<String> getRuleTypes(){
        return ruleMap.keySet();
    }

    public Class<?> getRuleClass(String ruleType){
        RuleWrapper ruleWrapper = ruleMap.get(ruleType);
        return ruleWrapper.getRuleClass();
    }
}
