package com.alibaba.csp.sentinel.datasource.kie.util;

import com.alibaba.csp.sentinel.datasource.kie.util.response.KieConfigResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class KieConfigClient {
    private final static CloseableHttpClient client = HttpClients.createDefault();

    public static Optional<KieConfigResponse> getConfig(String url){
        HttpGet httpGet = new HttpGet(url);
        KieConfigResponse kieResponse;
        try(CloseableHttpResponse response = client.execute(httpGet)) {

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK){
                log.warn(String.format("Get config from ServiceComb-kie failed, status code is %d",
                        statusCode));
                return Optional.empty();
            }

            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            kieResponse = JSON.parseObject(result, KieConfigResponse.class);
        } catch (IOException e){
            log.error("Get config from ServiceComb-kie failed.", e);
            return Optional.empty();
        }

        return Optional.ofNullable(kieResponse);
    }
}
