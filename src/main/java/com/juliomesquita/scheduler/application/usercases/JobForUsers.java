package com.juliomesquita.scheduler.application.usercases;

import com.juliomesquita.scheduler.domain.repositories.ProcessRepository;
import com.juliomesquita.scheduler.domain.repositories.UserRepository;
import com.juliomesquita.scheduler.infra.serviceExternal.ProcessClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

   @Scheduled()
   public void execute(){
      //TODO: Job Implementation
   }
}
