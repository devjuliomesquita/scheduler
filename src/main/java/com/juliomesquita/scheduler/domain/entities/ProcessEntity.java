package com.juliomesquita.scheduler.domain.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_process")
public class ProcessEntity {

   @Version
   private Long version;

   @Id
   @Column(name = "id", nullable = false)
   private UUID id;

   @Column(name = "number_process", nullable = false)
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
