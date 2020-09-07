package com.alibaba.csp.sentinel.datasource.rule;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RuleCenter {
    @Getter
    private static final ConcurrentHashMap<String, RuleWrapper> ruleMap =
            new ConcurrentHashMap<>();

    public static void register(String ruleName, RuleWrapper ruleWrapper){
        if (StringUtils.isEmpty(ruleName)){
            log.warn("Rule name is null.");
            return;
        }

        log.info(String.format("Register %s to rule center.", ruleName));
        ruleMap.put(ruleName, ruleWrapper);
    }

    public static void registerRuleManager(String ruleType, AbstractDataSource<String, ?> dataSource){
        if(!ruleMap.containsKey(ruleType)){
            log.error(String.format("Un support rule type %s.", ruleType));
            return;
        }

        log.info(String.format("Register %s to rule manager.", ruleType));
        RuleWrapper ruleWrapper = ruleMap.get(ruleType);
        ruleWrapper.registerRuleManager(dataSource);
    }
}
