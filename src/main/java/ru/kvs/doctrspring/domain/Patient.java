package ru.kvs.doctrspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;

@Entity
@Table(name = "patients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Patient extends Person {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    @GeneratedValue(strategy = GenerationType.TABLE)
    private PatientId id;

    @Column(name = "info")
    private String info;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "doctor_id"))
    private UserId doctorId;

    public void create(UserId doctorId) {
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

    @Override
    protected void generateId() {
        this.id = PatientId.newId();
    }

}
