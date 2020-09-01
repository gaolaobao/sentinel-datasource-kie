package com.demo.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.demo.sentinel.datasource.kie.util.KieClient;
import com.demo.sentinel.datasource.kie.util.response.KieConfigResponse;

import java.util.Optional;


public class KieDataSource<T> extends AutoRefreshDataSource<String, T> {
    public static final long DEFAULT_REFRESH_MS = 3000;

    private final String url;

    private String lastRule;

    public KieDataSource(Converter<String, T> configParser, String url) {
        this(configParser, DEFAULT_REFRESH_MS, url);
    }

    public KieDataSource(Converter<String, T> configParser, long recommendRefreshMs, String url) {
        super(configParser, recommendRefreshMs);
        this.url = url;
    }

    @Override
    public String readSource(){
        Optional<KieConfigResponse> config = KieClient.getConfig(this.url);

        // If return null, return last rule.
        this.lastRule = config.map(KieConfigResponse::getValue).orElse(lastRule);

        return this.lastRule;
    }
}
