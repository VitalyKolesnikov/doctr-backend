package ru.kvs.doctrspring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "clinics")
@Getter
@NoArgsConstructor
@SuperBuilder
public class Clinic extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private User doctor;

}
