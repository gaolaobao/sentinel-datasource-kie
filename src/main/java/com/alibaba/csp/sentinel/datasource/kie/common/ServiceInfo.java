package com.alibaba.csp.sentinel.datasource.kie.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "sentinel.source")
public class ServiceInfo {
    private String address;

    private String app;

    private String service;

    private String version;

    private String project;

    private String environment;

    public String getKieConfigUrl(){
        return "http://" + address + "/v1/" + project + "/kie/kv";
    }
}
