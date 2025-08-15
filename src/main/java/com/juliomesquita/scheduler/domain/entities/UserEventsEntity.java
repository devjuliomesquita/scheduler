package com.juliomesquita.scheduler.domain.entities;

import com.juliomesquita.scheduler.domain.enums.ProcessStatus;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_users_events")
public class UserEventsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "document", nullable = false)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProcessStatus status;

    @Column(name = "process_id")
    private UUID processId;

    public static UserEventsEntity create(final String name, final String document) {
        return new UserEventsEntity(
            name,
            document,
            ProcessStatus.PENDING,
            null
        );
    }

    public UserEventsEntity changeStatus(UUID processId) {
        this.status = ProcessStatus.PROCESSED;
        this.processId = processId;
        return this;
    }

    public UserEventsEntity markStatusIgnored() {
        this.status = ProcessStatus.IGNORED;
        return this;
    }

    private UserEventsEntity(String name, String document, ProcessStatus status, UUID processId) {
        this.name = name;
        this.document = document;
        this.status = status;
        this.processId = processId;
    }

    protected UserEventsEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
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
        UserEventsEntity that = (UserEventsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(
            name, that.name) && Objects.equals(
            document, that.document) && status == that.status && Objects.equals(
            processId, that.processId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, document, status, processId);
    }
}
