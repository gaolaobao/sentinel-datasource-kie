package com.alibaba.csp.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.kie.common.ServiceInfo;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@SpringBootTest
class KieDataSourceTest {

    KieDataSource<List<FlowRule>> kieDataSource;

    private String defaultRuleFile;

    @Autowired
    private ServiceInfo serviceInfo;

    @BeforeEach
    void init(){
        URL url = getClass().getClassLoader().getResource("default-rules.json");
        if (url != null){
            defaultRuleFile = url.getPath();
        }
    }

    @Test
    public void testParseYml(){
        Assertions.assertEquals(serviceInfo.getProject(), "default");
        Assertions.assertEquals(serviceInfo.getAddress(), "192.168.0.1:8080");
        Assertions.assertEquals(serviceInfo.getApp(), "appA");
        Assertions.assertEquals(serviceInfo.getService(), "serviceA");
        Assertions.assertEquals(serviceInfo.getVersion(), "1.0.0");

        Assertions.assertEquals(serviceInfo.getUrl(),  "http://192.168.0.1:8080/v1/default/kie/kv");
    }

    /**
     * Get rule by name.
     *
     * @param ruleKey rule name
     * @return rule value
     * @throws IOException file exception
     */
    public String getRule(String ruleKey) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();

        try(FileInputStream inputStream = new FileInputStream(defaultRuleFile)){
            int buf;
            while((buf = inputStream.read()) != -1){
                stringBuilder.append((char)buf);
            }
        }


        JSONObject jsonObject = JSON.parseObject(stringBuilder.toString());
        return jsonObject.get(ruleKey).toString();
    }

    @Test
    public void testDefaultFlowRule() throws IOException{
        String rule = getRule("FlowRule");

        Converter<String, List<FlowRule>> parser = source -> JSON.parseObject(source,
                new TypeReference<List<FlowRule>>() {});

        KieDataSource<List<FlowRule>> kieDataSource = new KieDataSource<>(parser, null,
                "FlowRule", rule);

        Assertions.assertFalse(StringUtils.isEmpty(rule));
        Assertions.assertEquals(kieDataSource.readSource(), rule);
    }
}