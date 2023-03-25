package ru.kvs.doctrspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static ru.kvs.doctrspring.domain.Status.ACTIVE;
import static ru.kvs.doctrspring.domain.Status.DELETED;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected Status status;

    public boolean isActive() {
        return this.status == ACTIVE;
    }

    public void onCreate() {
        generateId();
        var now = LocalDateTime.now();
        this.created = now;
        this.updated = now;
        this.status = ACTIVE;
    }

    protected abstract void generateId();

    public void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    public void softDelete() {
        this.onUpdate();
        this.status = DELETED;
    }

}
