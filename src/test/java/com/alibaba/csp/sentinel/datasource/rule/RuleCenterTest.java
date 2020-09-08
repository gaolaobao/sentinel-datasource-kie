package com.alibaba.csp.sentinel.datasource.rule;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class RuleCenterTest {
    @Autowired
    RuleCenter ruleCenter;

    @Test
    public void testRegisterRuleCenter(){
        Map<String, RuleWrapper> ruleMap = ruleCenter.getRuleMap();
        Assertions.assertThat(ruleMap.containsKey("ruleMap")).isTrue();
    }
}