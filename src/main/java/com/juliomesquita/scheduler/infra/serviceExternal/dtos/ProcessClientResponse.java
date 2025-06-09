package com.juliomesquita.scheduler.infra.serviceExternal.dtos;

import java.util.UUID;

public record ProcessClientResponse(
        UUID processId,
        String numberProcess,
        Long userId
) {
}
