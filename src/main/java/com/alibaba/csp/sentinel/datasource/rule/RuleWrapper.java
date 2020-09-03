package com.alibaba.csp.sentinel.datasource.rule;

public abstract class RuleWrapper {
    protected String ruleType;

    protected void registerRuleCenter(){
        RuleCenter.register(ruleType, this);
    }

    protected abstract void registerRuleManager(String ruleValue);
}
