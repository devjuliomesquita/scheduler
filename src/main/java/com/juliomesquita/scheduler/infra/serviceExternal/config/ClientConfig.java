package com.juliomesquita.scheduler.infra.serviceExternal.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;
@EnableRetry
@Configuration
public class ClientConfig {
   @Bean
   public RestClient restClient(RestClient.Builder builder) {
       var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
           .setMaxConnTotal(100)   // Máximo de conexões no pool
           .setMaxConnPerRoute(10) // Máximo por host/rota
           .setDefaultConnectionConfig(ConnectionConfig.custom()
               .setConnectTimeout(Timeout.ofSeconds(2)) // Tempo para abrir conexão
               .setSocketTimeout(Timeout.ofSeconds(4))  // Tempo de leitura
               .build())
           .build();

       var requestConfig = RequestConfig.custom()
           .setConnectionRequestTimeout(Timeout.ofSeconds(2)) // Tempo para pegar conexão do pool
           .build();

       var httpClient = HttpClients.custom()
           .setConnectionManager(connectionManager)
           .setDefaultRequestConfig(requestConfig)
           .evictIdleConnections(TimeValue.ofSeconds(30)) // Fecha conexões ociosas
           .build();

       return builder
           .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
           .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
           .build();
   }
}
