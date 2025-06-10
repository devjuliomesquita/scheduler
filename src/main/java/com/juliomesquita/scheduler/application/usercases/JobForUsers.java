package com.juliomesquita.scheduler.application.usercases;

import com.juliomesquita.scheduler.domain.UserAggregate;
import com.juliomesquita.scheduler.domain.entities.ProcessEntity;
import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import com.juliomesquita.scheduler.domain.repositories.ProcessRepository;
import com.juliomesquita.scheduler.domain.repositories.UserRepository;
import com.juliomesquita.scheduler.infra.serviceExternal.ProcessClient;
import com.juliomesquita.scheduler.infra.serviceExternal.dtos.ProcessClientResponse;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class JobForUsers {
   private final UserRepository userRepository;
   private final ProcessRepository processRepository;
   private final ProcessClient processClient;

   public JobForUsers(
           final UserRepository userRepository,
           final ProcessRepository processRepository,
           final ProcessClient processClient) {
      this.userRepository = Objects.requireNonNull(userRepository);
      this.processRepository = Objects.requireNonNull(processRepository);
      this.processClient = Objects.requireNonNull(processClient);
   }

   @Transactional
   @Scheduled(cron = "0 0 12 * * *") // "0 0 12 * * *" = todo dia às 12:00:00 (meio-dia)
   public void execute(){
      final List<UserAggregate> users = this.userRepository.findByStatus(ProcessStatus.PENDING);
      users.forEach(user -> {
         final ProcessClientResponse responseClient = this.processClient.getInfoProcess(user.getId());

         final ProcessEntity process = ProcessEntity.createProcess(
                 responseClient.processId(), responseClient.numberProcess());
         this.processRepository.save(process);

         user.changeStatus(process.getId());
         this.userRepository.save(user);
      });
   }
}
