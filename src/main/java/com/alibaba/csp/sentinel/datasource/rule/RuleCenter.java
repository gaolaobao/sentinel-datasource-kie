package com.alibaba.csp.sentinel.datasource.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RuleCenter {
    private static final ConcurrentHashMap<String, RuleWrapper> map = new ConcurrentHashMap<>();

    public static void register(String ruleName, RuleWrapper ruleWrapper){
        if (StringUtils.isEmpty(ruleName)){
            log.warn("Rule name is null.");
            return;
        }

        map.put(ruleName, ruleWrapper);
    }

    public static void registerRuleManager(String ruleType, String ruleValue){
        if(!map.containsKey(ruleType)){
            log.error(String.format("Un support rule type %s.", ruleType));
            return;
        }

        RuleWrapper ruleWrapper = map.get(ruleType);
        ruleWrapper.registerRuleManager(ruleValue);
    }
}
