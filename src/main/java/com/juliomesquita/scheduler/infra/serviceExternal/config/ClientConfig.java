package com.juliomesquita.scheduler.infra.serviceExternal.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {
   @Bean
   public RestClient restClient(RestClient.Builder builder) {
      final var connection = ConnectionConfig.custom()
          .setConnectTimeout(Timeout.ofSeconds(2))
          .setSocketTimeout(Timeout.ofSeconds(4))
          .build();
      var connectionManager = new BasicHttpClientConnectionManager();
      connectionManager.setConnectionConfig(connection);

      final var request = RequestConfig.custom()
          .setConnectionRequestTimeout(Timeout.ofSeconds(2))
          .build();

      final var httpClient = HttpClients.custom()
          .setDefaultRequestConfig(request)
          .setConnectionManager(connectionManager)
          .build();
      final var factory = new HttpComponentsClientHttpRequestFactory(httpClient);
      return builder
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .requestFactory(factory)
          .build();
   }
}
