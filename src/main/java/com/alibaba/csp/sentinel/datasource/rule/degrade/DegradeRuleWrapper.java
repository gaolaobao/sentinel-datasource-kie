package com.alibaba.csp.sentinel.datasource.rule.degrade;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.rule.RuleWrapper;
import com.alibaba.csp.sentinel.property.SentinelProperty;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("DegradeRule")
public class DegradeRuleWrapper extends RuleWrapper {

    @Override
    public void registerRuleManager(AbstractDataSource<String, ?> dataSource) {
        DegradeRuleManager.register2Property((SentinelProperty<List<DegradeRule>>) dataSource.getProperty());
    }

    @Override
    protected Class<?> getRuleClass() {
        return DegradeRule.class;
    }
}
