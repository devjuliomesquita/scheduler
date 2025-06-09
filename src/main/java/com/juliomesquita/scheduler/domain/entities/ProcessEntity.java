package com.juliomesquita.scheduler.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

import javax.swing.*;
import java.util.UUID;

@Entity
public class ProcessEntity {

   @Version
   private Long version;

   @Id
   private UUID id;

   private Spring numberProcess;
}
