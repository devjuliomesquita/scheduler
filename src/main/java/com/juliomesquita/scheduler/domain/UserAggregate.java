package com.juliomesquita.scheduler.domain;

import com.juliomesquita.scheduler.domain.entities.ProcessEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
import java.util.Set;

@Entity
public class UserAggregate {

   @Id
   private Long id;

   private String name;

   private String email;
}
