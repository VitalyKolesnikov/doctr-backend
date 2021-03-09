package ru.kvs.doctrspring.builders;

import ru.kvs.doctrspring.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class VisitBuilder {
    private Long id;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();
    private Status status = Status.ACTIVE;
    private User doctor;
    private Patient patient;
    private Clinic clinic;
    private LocalDate date;
    private Integer cost;
    private Integer percent;
    private Boolean child;
    private Boolean first;
    private String info;

    private VisitBuilder() {
    }

    public static VisitBuilder aVisit() {
        return new VisitBuilder();
    }

    public VisitBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public VisitBuilder created(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public VisitBuilder updated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public VisitBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public VisitBuilder doctor(User doctor) {
        this.doctor = doctor;
        return this;
    }

    public VisitBuilder patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public VisitBuilder clinic(Clinic clinic) {
        this.clinic = clinic;
        return this;
    }

    public VisitBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public VisitBuilder cost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public VisitBuilder percent(Integer percent) {
        this.percent = percent;
        return this;
    }

    public VisitBuilder child(Boolean child) {
        this.child = child;
        return this;
    }

    public VisitBuilder first(Boolean first) {
        this.first = first;
        return this;
    }

    public VisitBuilder info(String info) {
        this.info = info;
        return this;
    }

    public Visit build() {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setCreated(created);
        visit.setUpdated(updated);
        visit.setStatus(status);
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        visit.setClinic(clinic);
        visit.setDate(date);
        visit.setCost(cost);
        visit.setPercent(percent);
        visit.setChild(child);
        visit.setFirst(first);
        visit.setInfo(info);
        return visit;
    }
}
