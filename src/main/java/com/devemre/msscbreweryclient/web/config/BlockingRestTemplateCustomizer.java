package com.devemre.msscbreweryclient.web.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    private final Integer MAX_TOTAL_CONNECTIONS;
    private final Integer DEFAULT_MAX_TOTAL_CONNECTIONS;
    private final Integer CONNECTION_REQUEST_TIMEOUT;
    private final Integer RESPONSE_TIMEOUT;

    public BlockingRestTemplateCustomizer(
            @Value("${sfg.maxtotalconnections}") Integer MAX_TOTAL_CONNECTIONS,
            @Value("${sfg.defaultmaxtotalconnections}") Integer DEFAULT_MAX_TOTAL_CONNECTIONS,
            @Value("${sfg.connectionrequesttimeout}") Integer CONNECTION_REQUEST_TIMEOUT,
            @Value("${sfg.responsetimeout}") Integer RESPONSE_TIMEOUT
    ) {
        this.MAX_TOTAL_CONNECTIONS = MAX_TOTAL_CONNECTIONS;
        this.DEFAULT_MAX_TOTAL_CONNECTIONS = DEFAULT_MAX_TOTAL_CONNECTIONS;
        this.CONNECTION_REQUEST_TIMEOUT = CONNECTION_REQUEST_TIMEOUT;
        this.RESPONSE_TIMEOUT = RESPONSE_TIMEOUT;
    }

    public ClientHttpRequestFactory clientHttpRequestFactory(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_TOTAL_CONNECTIONS);
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(CONNECTION_REQUEST_TIMEOUT))
                .setResponseTimeout(Timeout.ofMilliseconds(RESPONSE_TIMEOUT))
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    }
}