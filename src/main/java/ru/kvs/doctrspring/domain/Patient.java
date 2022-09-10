package ru.kvs.doctrspring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
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
