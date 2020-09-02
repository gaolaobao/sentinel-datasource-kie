package com.alibaba.csp.sentinel.datasource.kie.util.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;

import java.util.List;

@Getter
public class KieConfigResponse {
    @JSONField(name = "total")
    private int total;

    @JSONField(name = "data")
    private List<KieConfigItem> data;
}
