package com.alibaba.csp.sentinel.datasource.rule.flow;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.rule.RuleWrapper;
import com.alibaba.csp.sentinel.property.SentinelProperty;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("FlowRule")
public class FlowRuleWrapper extends RuleWrapper {

    @Override
    public void registerRuleManager(AbstractDataSource<String, ?> dataSource) {
        FlowRuleManager.register2Property((SentinelProperty<List<FlowRule>>) dataSource.getProperty());
    }
}
