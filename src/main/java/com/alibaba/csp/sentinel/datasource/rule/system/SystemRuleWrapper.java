package com.alibaba.csp.sentinel.datasource.rule.system;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.rule.RuleWrapper;
import com.alibaba.csp.sentinel.property.SentinelProperty;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("SystemRule")
public class SystemRuleWrapper extends RuleWrapper {

    @Override
    public void registerRuleManager(AbstractDataSource<String, ?> dataSource) {
        SystemRuleManager.register2Property((SentinelProperty<List<SystemRule>>) dataSource.getProperty());
    }
}
