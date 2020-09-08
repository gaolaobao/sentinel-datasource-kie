package com.alibaba.csp.sentinel.datasource.rule;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;

public abstract class RuleWrapper {
    protected abstract void registerRuleManager(AbstractDataSource<String, ?> dataSource);
}
