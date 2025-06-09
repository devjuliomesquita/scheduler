package com.juliomesquita.scheduler.infra.serviceExternal.implementation;

import com.juliomesquita.scheduler.infra.serviceExternal.ProcessClient;
import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ProcessClientImpl implements ProcessClient {
   @Override
   public ProcessClientResponse getInfoProcess(Long userId) {
      return null;
   }
}
