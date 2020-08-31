package com.demo.sentinel.datasource.kie;

import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;


public class KieDataSource<T> extends AutoRefreshDataSource<String, T> {
    public static final long DEFAULT_REFRESH_MS = 3000;

    private String url;

    public KieDataSource(Converter<String, T> configParser, String url) {
        this(configParser, DEFAULT_REFRESH_MS, url);
    }

    public KieDataSource(Converter<String, T> configParser, long recommendRefreshMs, String url) {
        super(configParser, recommendRefreshMs);
        this.url = url;
    }

    @Override
    public String readSource(){
        return null;
    }
}
