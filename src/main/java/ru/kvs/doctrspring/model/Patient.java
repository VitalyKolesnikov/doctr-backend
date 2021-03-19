package ru.kvs.doctrspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@SuperBuilder
public class Patient extends Person {

    @Column(name = "info")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private User doctor;

}
