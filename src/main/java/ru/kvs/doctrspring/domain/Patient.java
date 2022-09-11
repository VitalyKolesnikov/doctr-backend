package ru.kvs.doctrspring.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "patients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Patient extends Person {

    @Column(name = "info")
    private String info;

    @JoinColumn(name = "doctor_id")
    private Long doctorId;

    public void create(Long doctorId) {
        this.doctorId = doctorId;
        this.onCreate();
    }

    public void update(Patient patient) {
        this.firstName = patient.getFirstName();
        this.middleName = patient.getMiddleName();
        this.lastName = patient.getLastName();
        this.birthDate = patient.getBirthDate();
        this.email = patient.getEmail();
        this.phone = patient.getPhone();
        this.info = patient.getInfo();
        this.onUpdate();
    }

}
