package com.alibaba.csp.sentinel.datasource.rule.flowrule;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.kie.KieDataSource;
import com.alibaba.csp.sentinel.datasource.rule.RuleWrapper;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class FlowRuleWrapper extends RuleWrapper {
    @PostConstruct
    public void init(){
        this.ruleType = "FlowType";
        registerRuleCenter();
    }

    @Override
    public void registerRuleManager(String ruleValue) {
        Converter<String, List<FlowRule>> parser = source -> JSON.parseObject(source,
                new TypeReference<List<FlowRule>>() {});

        KieDataSource<List<FlowRule>> dataSource = new KieDataSource<>(parser, null,
                this.ruleType, ruleValue);

        JSON.parseObject(null, String.class);
        FlowRuleManager.register2Property((dataSource).getProperty());
    }
}
