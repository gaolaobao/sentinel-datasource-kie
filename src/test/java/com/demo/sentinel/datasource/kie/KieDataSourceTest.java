package com.demo.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class KieDataSourceTest {

    KieDataSource<List<FlowRule>> kieDataSource;

    @Test
    public void testParseYml(){

    }
}