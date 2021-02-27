package ru.kvs.doctrspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;
import ru.kvs.doctrspring.HasId;

import javax.persistence.*;
import java.util.Date;

/**
 * Base class with property 'id'.
 * Used as a base class for all objects that requires this property.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@MappedSuperclass
@Data
public abstract class BaseEntity implements HasId {

    public static final int START_SEQ = 1000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Long id;

    @CreatedDate
    @Column(name = "created")
    @JsonIgnore
    private Date created = new Date();

    @LastModifiedDate
    @Column(name = "updated")
    @JsonIgnore
    private Date updated = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

}
