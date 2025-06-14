package com.juliomesquita.scheduler.domain.repositories;

import com.juliomesquita.scheduler.domain.UserAggregate;
import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserAggregate, Long> {

   @QueryHints({
           @QueryHint(name = "javax.persistence.lock.timeout", value = "-2")
   })
   @Lock(LockModeType.PESSIMISTIC_WRITE)
   List<UserAggregate> findTop50ByStatus(ProcessStatus status);
}
