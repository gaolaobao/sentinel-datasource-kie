package com.alibaba.csp.sentinel.datasource.rule;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class RuleCenterTest {
    @Test
    public void testRegisterRuleCenter(){
        ConcurrentHashMap<String, RuleWrapper> ruleMap = RuleCenter.getRuleMap();
        Assertions.assertThat(ruleMap.containsKey("ruleMap")).isTrue();
    }
}