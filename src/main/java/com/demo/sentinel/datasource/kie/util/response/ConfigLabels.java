package com.demo.sentinel.datasource.kie.util.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;

@Getter
public class ConfigLabels {
    @JSONField(name = "app")
    private String app;

    @JSONField(name = "service")
    private String service;

    @JSONField(name = "version")
    private String version;
}
