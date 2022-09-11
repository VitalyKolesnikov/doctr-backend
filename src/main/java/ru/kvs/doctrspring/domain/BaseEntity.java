package ru.kvs.doctrspring.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.kvs.doctrspring.domain.Status.ACTIVE;
import static ru.kvs.doctrspring.domain.Status.DELETED;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity {

    public static final int START_SEQ = 1000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @EqualsAndHashCode.Include
    private Long id;

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
        var now = LocalDateTime.now();
        this.created = now;
        this.updated = now;
        this.status = ACTIVE;
    }

    public void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    public void softDelete() {
        this.onUpdate();
        this.status = DELETED;
    }

}
