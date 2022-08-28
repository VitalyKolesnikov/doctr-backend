package ru.kvs.doctrspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.kvs.doctrspring.HasId;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements HasId {

    public static final int START_SEQ = 1000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Long id;

    @CreatedDate
    @Column(name = "created")
    @JsonIgnore
    private LocalDateTime created = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated")
    @JsonIgnore
    private LocalDateTime updated = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @JsonIgnore
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

}
