package com.alibaba.csp.sentinel.datasource.rule;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;

public abstract class RuleWrapper {
    protected String ruleType;

    protected void registerRuleCenter(){
        RuleCenter.register(ruleType, this);
    }

    protected abstract void registerRuleManager(AbstractDataSource<String, ?> dataSource);
}
