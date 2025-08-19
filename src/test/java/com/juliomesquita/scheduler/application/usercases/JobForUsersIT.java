package com.juliomesquita.scheduler.application.usercases;

import com.juliomesquita.scheduler.application.services.ProcessService;
import com.juliomesquita.scheduler.domain.entities.ProcessEntity;
import com.juliomesquita.scheduler.domain.entities.UserEventsEntity;
import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import com.juliomesquita.scheduler.domain.repositories.ProcessRepository;
import com.juliomesquita.scheduler.domain.repositories.UserRepository;
import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class JobForUsersIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private ProcessService processService;

    private JobForUsers jobForUsers;

    @BeforeEach
    void setup() {
        processService = mock(ProcessService.class);
        jobForUsers = new JobForUsers(userRepository, processRepository, processService, transactionTemplate);
    }

    @Test
    void deveProcessarUsuariosPendentesESalvarProcesso() {
        // Arrange
        UserEventsEntity user = UserEventsEntity.create("Usuário Teste", "12345678900");
        userRepository.save(user);

        ProcessClientResponse response = new ProcessClientResponse(UUID.randomUUID(), "12345", user.getId());
        when(processService.fetchProcessClient(user.getId())).thenReturn(response);

        // Act
        jobForUsers.execute();

        // Assert
        UserEventsEntity atualizado = userRepository.findById(user.getId()).orElseThrow();
        assertThat(atualizado.getStatus()).isNotEqualTo(ProcessStatus.PENDING);

        ProcessEntity process = processRepository.findAll().stream().findFirst().orElse(null);
        assertThat(process).isNotNull();
        assertThat(process.getNumberProcess()).isEqualTo("12345");
    }

    @Test
    void deveMarcarUsuarioComoIgnoradoEmErro4xx() {
        UserEventsEntity user = UserEventsEntity.create("Usuário Erro 4xx", "99999999999");
        userRepository.save(user);

        when(processService.fetchProcessClient(user.getId()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        jobForUsers.execute();

        UserEventsEntity atualizado = userRepository.findById(user.getId()).orElseThrow();
        assertThat(atualizado.getStatus()).isEqualTo(ProcessStatus.IGNORED);
        assertThat(processRepository.findAll()).isEmpty();
    }

    @Test
    void deveApenasLogarEmErroGenerico() {
        UserEventsEntity user = UserEventsEntity.create("Usuário Erro Genérico", "88888888888");

        // Mock puro do UserRepository
        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.findTop50ByStatus(ProcessStatus.PENDING))
            .thenReturn(java.util.List.of(user))
            .thenReturn(java.util.List.of());
        when(mockRepo.save(user)).thenReturn(user);
        when(mockRepo.findById(user.getId())).thenReturn(java.util.Optional.of(user));

        when(processService.fetchProcessClient(user.getId()))
            .thenThrow(new RuntimeException("Erro inesperado"));

        jobForUsers = new JobForUsers(mockRepo, processRepository, processService, transactionTemplate);

        jobForUsers.execute();

        UserEventsEntity atualizado = mockRepo.findById(user.getId()).orElseThrow();
        assertThat(atualizado.getStatus()).isEqualTo(ProcessStatus.PENDING);
        assertThat(processRepository.findAll()).isEmpty();
    }

    @TestConfiguration
    static class Config {
        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager tm) {
            return new TransactionTemplate(tm);
        }
    }
}