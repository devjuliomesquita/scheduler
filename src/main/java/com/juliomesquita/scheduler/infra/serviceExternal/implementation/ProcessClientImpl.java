package com.juliomesquita.scheduler.infra.serviceExternal.implementation;

import com.juliomesquita.scheduler.infra.serviceExternal.ProcessClient;
import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class ProcessClientImpl implements ProcessClient {
   private final RestClient restClient;

   public ProcessClientImpl(final RestClient restClient) {
      this.restClient = Objects.requireNonNull(restClient);
   }

   @Override
   public ProcessClientResponse getInfoProcess(final Long userId) {
      return this.restClient
          .get()
          .uri("/{userId}", userId)
          .retrieve()
          .body(ProcessClientResponse.class);
   }
}
