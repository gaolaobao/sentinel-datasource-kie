package com.alibaba.csp.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.kie.common.ServiceInfo;
import com.alibaba.csp.sentinel.datasource.kie.util.KieConfigClient;
import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigItem;
import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigLabels;
import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class KieDataSource<T> extends AutoRefreshDataSource<String, T> {
    public static final long DEFAULT_REFRESH_MS = 3000;

    private final ServiceInfo serviceInfo;

    private final String ruleKey;

    private String lastRules;

    public KieDataSource(Converter<String, T> configParser, ServiceInfo serviceInfo, String ruleName,
                         String defaultRule) {
        this(configParser, DEFAULT_REFRESH_MS, serviceInfo, ruleName, defaultRule);
    }

    public KieDataSource(Converter<String, T> configParser, long recommendRefreshMs,
                         ServiceInfo serviceInfo, String ruleKey, String defaultRule) {
        super(configParser, recommendRefreshMs);

        this.serviceInfo = serviceInfo;
        this.ruleKey = ruleKey;
        this.lastRules = defaultRule;

        firstLoad();
    }

    private void firstLoad() {
        log.info(String.format("First load config, ruleKey: %s.", this.ruleKey));

        try {
            getProperty().updateValue(loadConfig(this.lastRules));
        } catch (Throwable e) {
            log.error("First loadConfig exception", e);
        }
    }

    @Override
    public String readSource() {
        if (serviceInfo == null || StringUtils.isEmpty(serviceInfo.getAddress())){
            return lastRules;
        }

        Optional<KieConfigResponse> config = KieConfigClient.getConfig(serviceInfo.getKieConfigUrl());

        config.ifPresent(x -> {
            List<JSONObject> strList = x.getData().stream()
                    .filter(this::isTargetItem)
                    .map(item ->JSON.parseObject(item.getValue()))
                    .collect(Collectors.toList());

            this.lastRules = JSON.toJSONString(strList);
        });

        return lastRules;
    }

    /**
     * Judge target item by conditions.
     *
     * @param item config item
     * @return judge result
     */
    private boolean isTargetItem(KieConfigItem item){
        KieConfigLabels labels = item.getLabels();
        String key = item.getKey();

        return this.ruleKey.equals(key)
                && serviceInfo.getApp().equals(labels.getApp())
                && serviceInfo.getService().equals(labels.getService())
                && serviceInfo.getVersion().equals(labels.getVersion())
                && serviceInfo.getEnvironment().equals(labels.getEnvironment());
    }
}
