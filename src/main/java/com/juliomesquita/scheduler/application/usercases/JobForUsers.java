package com.juliomesquita.scheduler.application.usercases;

import com.juliomesquita.scheduler.domain.UserAggregate;
import com.juliomesquita.scheduler.domain.entities.ProcessEntity;
import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import com.juliomesquita.scheduler.domain.repositories.ProcessRepository;
import com.juliomesquita.scheduler.domain.repositories.UserRepository;
import com.juliomesquita.scheduler.infra.serviceExternal.ProcessClient;
import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Objects;

@Component
public class JobForUsers {
   private final static Logger log = LoggerFactory.getLogger(JobForUsers.class);
   private final UserRepository userRepository;
   private final ProcessRepository processRepository;
   private final ProcessClient processClient;
   private final TransactionTemplate transactionTemplate;

   public JobForUsers(
       final UserRepository userRepository,
       final ProcessRepository processRepository,
       final ProcessClient processClient,
       final TransactionTemplate transactionTemplate) {
      this.userRepository = Objects.requireNonNull(userRepository);
      this.processRepository = Objects.requireNonNull(processRepository);
      this.processClient = Objects.requireNonNull(processClient);
      this.transactionTemplate = Objects.requireNonNull(transactionTemplate);
   }

   @Scheduled(cron = "0 0 12 * * *") // "0 0 12 * * *" = todo dia às 12:00:00 (meio-dia)
   public void execute() {
      boolean pending = true;
      while (pending) {
         pending = Boolean.TRUE.equals(this.transactionTemplate.execute(transaction -> {
            final List<UserAggregate> users = this.userRepository.findTop50ByStatus(ProcessStatus.PENDING);
            if (users.isEmpty()) {
               return false;
            }

            users.forEach(user -> {
               try {
                  final ProcessClientResponse responseClient = this.fetchProcessClient(user.getId());

                  final ProcessEntity process = ProcessEntity.createProcess(
                      responseClient.processId(), responseClient.numberProcess());
                  this.processRepository.save(process);

                  user.changeStatus(process.getId());
                  this.userRepository.save(user);

               } catch (HttpClientErrorException ex) {
                  if (ex.getStatusCode().is4xxClientError()) {
                     log.warn("Family Error 4xxx to the user {}, marked user as ignored.", user.getId());
                     user.markStatusIgnored();
                     this.userRepository.save(user);
                  }
               } catch (Exception e) {
                  log.warn("Error when trying to pick up the user {}, error: {}", user.getId(), e.getMessage());
               }
            });
            return true;
         }));
      }
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
   private ProcessClientResponse fetchProcessClient(final Long userId) {
      try {
         return this.processClient.getInfoProcess(userId);
      } catch (Exception e) {
         log.warn("Error when searching for data... | message: {}", e.getMessage());
         throw e;
      }
   }
}
