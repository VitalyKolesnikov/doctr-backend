package ru.kvs.doctrspring.builders;

import ru.kvs.doctrspring.model.Clinic;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.User;

import java.time.LocalDateTime;

public final class ClinicBuilder {
    private Long id;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();
    private Status status = Status.ACTIVE;
    private String name;
    private String phone;
    private String address;
    private User doctor;

    private ClinicBuilder() {
    }

    public static ClinicBuilder aClinic() {
        return new ClinicBuilder();
    }

    public ClinicBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ClinicBuilder created(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public ClinicBuilder updated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public ClinicBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public ClinicBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClinicBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public ClinicBuilder address(String address) {
        this.address = address;
        return this;
    }

    public ClinicBuilder doctor(User doctor) {
        this.doctor = doctor;
        return this;
    }

    public Clinic build() {
        Clinic clinic = new Clinic();
        clinic.setId(id);
        clinic.setCreated(created);
        clinic.setUpdated(updated);
        clinic.setStatus(status);
        clinic.setName(name);
        clinic.setPhone(phone);
        clinic.setAddress(address);
        clinic.setDoctor(doctor);
        return clinic;
    }
}
