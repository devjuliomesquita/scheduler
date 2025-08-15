package com.juliomesquita.scheduler.application.services;

import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;

public interface ProcessService {
    ProcessClientResponse fetchProcessClient(Long userId);
}
