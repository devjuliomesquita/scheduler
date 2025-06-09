package com.juliomesquita.scheduler.infra.serviceExternal;

import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;

public interface ProcessClient {

   ProcessClientResponse getInfoProcess(Long userId);

}
