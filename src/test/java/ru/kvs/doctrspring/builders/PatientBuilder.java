package ru.kvs.doctrspring.builders;

import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public final class PatientBuilder {
    private Long id;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();
    private Status status = Status.ACTIVE;
    private String info;
    private User doctor;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;

    private PatientBuilder() {
    }

    public static PatientBuilder aPatient() {
        return new PatientBuilder();
    }

    public PatientBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PatientBuilder created(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public PatientBuilder updated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public PatientBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public PatientBuilder info(String info) {
        this.info = info;
        return this;
    }

    public PatientBuilder doctor(User doctor) {
        this.doctor = doctor;
        return this;
    }

    public PatientBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PatientBuilder middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public PatientBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PatientBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PatientBuilder email(String email) {
        this.email = email;
        return this;
    }

    public PatientBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public Patient build() {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setCreated(created);
        patient.setUpdated(updated);
        patient.setStatus(status);
        patient.setInfo(info);
        patient.setDoctor(doctor);
        patient.setFirstName(firstName);
        patient.setMiddleName(middleName);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setEmail(email);
        patient.setPhone(phone);
        return patient;
    }
}
