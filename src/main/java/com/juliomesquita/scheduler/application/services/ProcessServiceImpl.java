package com.juliomesquita.scheduler.application.services;

import com.juliomesquita.scheduler.infra.serviceExternal.ProcessClient;
import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Objects;

@Component
public class ProcessServiceImpl implements ProcessService {
    private static final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);
    private final ProcessClient processClient;

    public ProcessServiceImpl(final ProcessClient processClient) {
        this.processClient = Objects.requireNonNull(processClient);
    }

    @Retryable(
        maxAttempts = 3,
        backoff = @Backoff(delay = 1500, multiplier = 2),
        retryFor = {
            HttpServerErrorException.class,
            ResourceAccessException.class
        },
        noRetryFor = {
            HttpClientErrorException.BadRequest.class,
            HttpClientErrorException.NotFound.class,
            HttpClientErrorException.Unauthorized.class,
            HttpClientErrorException.Forbidden.class,
        }
    )
    public ProcessClientResponse fetchProcessClient(final Long userId) {
        try {
            return this.processClient.getInfoProcess(userId);
        } catch (Exception e) {
            log.warn("Error when searching for data... | message: {}", e.getMessage());
            throw e;
        }
    }
}
