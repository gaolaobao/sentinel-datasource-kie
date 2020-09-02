package com.alibaba.csp.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.kie.common.ServiceInfo;
import com.alibaba.csp.sentinel.datasource.kie.util.KieClient;
import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigItem;
import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigLabels;
import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    }

    @Override
    public String readSource() {
        if (serviceInfo == null || StringUtils.isEmpty(serviceInfo.getUrl())){
            return lastRules;
        }

        Optional<KieConfigResponse> config = KieClient.getConfig(serviceInfo.getUrl());

        // Get target items value.
        List<String> newRules = config.map(
                response -> response.getData().stream()
                        .filter(this::isTargetItem)
                        .map(KieConfigItem::getValue)
                        .collect(Collectors.toList()))
                .orElse(null);


        // If null, return last rule.
        lastRules = (newRules == null) ? lastRules : JSON.toJSONString(newRules);

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

        return key.equals(this.ruleKey)
                && labels.getApp().equals(serviceInfo.getApp())
                && labels.getService().equals(serviceInfo.getService())
                && labels.getVersion().equals(serviceInfo.getVersion());
    }
}
