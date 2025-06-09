package com.juliomesquita.scheduler.domain;

import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class UserAggregate {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;

   private String email;

   @Enumerated(EnumType.STRING)
   private ProcessStatus status;

   public static UserAggregate create(final String name, final String email) {
      return new UserAggregate(
              name,
              email,
              ProcessStatus.PENDING
      );
   }

   public UserAggregate changeStatus() {
      this.status = ProcessStatus.PROCESSED;
      return this;
   }

   private UserAggregate(final String name, final String email, final ProcessStatus status) {
      this.name = name;
      this.email = email;
      this.status = status;
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

   @Override
   public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      UserAggregate that = (UserAggregate) o;
      return Objects.equals(id, that.id) && Objects.equals(
              name, that.name) && Objects.equals(email, that.email) && status == that.status;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name, email, status);
   }
}
