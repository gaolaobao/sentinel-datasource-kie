package com.alibaba.csp.sentinel.datasource.rule.authority;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.rule.RuleWrapper;
import com.alibaba.csp.sentinel.property.SentinelProperty;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("AuthorityRule")
public class AuthorityRuleWrapper extends RuleWrapper {

    @Override
    public void registerRuleManager(AbstractDataSource<String, ?> dataSource) {
        AuthorityRuleManager.register2Property((SentinelProperty<List<AuthorityRule>>) dataSource.getProperty());
    }

    @Override
    protected Class<?> getRuleClass() {
        return AuthorityRule.class;
    }
}

