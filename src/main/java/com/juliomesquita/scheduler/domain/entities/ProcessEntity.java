package com.juliomesquita.scheduler.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

import javax.swing.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class ProcessEntity {

   @Version
   private Long version;

   @Id
   private UUID id;

   private String numberProcess;

   public static ProcessEntity createProcess(final UUID id, final String numberProcess) {
      return new ProcessEntity(id, numberProcess);
   }

   private ProcessEntity(final UUID id, final String numberProcess) {
      this.id = id;
      this.numberProcess = numberProcess;
   }

   protected ProcessEntity() {
   }

   public Long getVersion() {
      return version;
   }

   public UUID getId() {
      return id;
   }

   public String getNumberProcess() {
      return numberProcess;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      ProcessEntity that = (ProcessEntity) o;
      return Objects.equals(version, that.version) && Objects.equals(
              id, that.id) && Objects.equals(numberProcess, that.numberProcess);
   }

   @Override
   public int hashCode() {
      return Objects.hash(version, id, numberProcess);
   }
}
