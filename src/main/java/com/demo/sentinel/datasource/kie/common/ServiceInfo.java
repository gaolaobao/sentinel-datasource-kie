package com.demo.sentinel.datasource.kie.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceInfo {
    private final String address;

    private String app;

    private String service;

    private String version;

    private String project;

    public String getUrl(){
        return address + "/v1/" + project + "/kie/kv";
    }
}
