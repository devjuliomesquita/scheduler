package com.juliomesquita.scheduler.domain.repositories;

import com.juliomesquita.scheduler.domain.UserAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAggregate, Long> {
}
