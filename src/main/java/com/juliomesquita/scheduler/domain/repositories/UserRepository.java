package com.juliomesquita.scheduler.domain.repositories;

import com.juliomesquita.scheduler.domain.UserAggregate;
import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserAggregate, Long> {
   List<UserAggregate> findByStatus(ProcessStatus status);
}
