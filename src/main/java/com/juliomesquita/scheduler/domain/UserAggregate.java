package com.juliomesquita.scheduler.domain;

import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
public class UserAggregate {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;

   private String email;

   @Enumerated(EnumType.STRING)
   private ProcessStatus status;

   private UUID processId;

   public static UserAggregate create(final String name, final String email) {
      return new UserAggregate(
              name,
              email,
              ProcessStatus.PENDING,
              null
      );
   }

   public UserAggregate changeStatus(UUID processId) {
      this.status = ProcessStatus.PROCESSED;
      this.processId = processId;
      return this;
   }

   private UserAggregate(String name, String email, ProcessStatus status, UUID processId) {
      this.name = name;
      this.email = email;
      this.status = status;
      this.processId = processId;
   }

   public UserAggregate() {
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getEmail() {
      return email;
   }

   public ProcessStatus getStatus() {
      return status;
   }

   public UUID getProcessId() {
      return processId;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      UserAggregate that = (UserAggregate) o;
      return Objects.equals(id, that.id) && Objects.equals(
              name, that.name) && Objects.equals(
              email, that.email) && status == that.status && Objects.equals(
              processId, that.processId);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, email, status, processId);
   }
}
