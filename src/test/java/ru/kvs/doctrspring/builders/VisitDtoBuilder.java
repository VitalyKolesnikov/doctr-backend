package ru.kvs.doctrspring.builders;

import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.model.Status;

import java.time.LocalDate;
import java.util.Date;

public final class VisitDtoBuilder {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
    private Long clinicId;
    private Long patientId;
    private LocalDate date;
    private Integer cost;
    private Integer percent;
    private Boolean child;
    private Boolean first;
    private String info;

    private VisitDtoBuilder() {
    }

    public static VisitDtoBuilder aVisitDto() {
        return new VisitDtoBuilder();
    }

    public VisitDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public VisitDtoBuilder created(Date created) {
        this.created = created;
        return this;
    }

    public VisitDtoBuilder updated(Date updated) {
        this.updated = updated;
        return this;
    }

    public VisitDtoBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public VisitDtoBuilder clinicId(Long clinicId) {
        this.clinicId = clinicId;
        return this;
    }

    public VisitDtoBuilder patientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public VisitDtoBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public VisitDtoBuilder cost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public VisitDtoBuilder percent(Integer percent) {
        this.percent = percent;
        return this;
    }

    public VisitDtoBuilder child(Boolean child) {
        this.child = child;
        return this;
    }

    public VisitDtoBuilder first(Boolean first) {
        this.first = first;
        return this;
    }

    public VisitDtoBuilder info(String info) {
        this.info = info;
        return this;
    }

    public VisitDto build() {
        VisitDto visitDto = new VisitDto();
        visitDto.setId(id);
        visitDto.setCreated(created);
        visitDto.setUpdated(updated);
        visitDto.setStatus(status);
        visitDto.setClinicId(clinicId);
        visitDto.setPatientId(patientId);
        visitDto.setDate(date);
        visitDto.setCost(cost);
        visitDto.setPercent(percent);
        visitDto.setChild(child);
        visitDto.setFirst(first);
        visitDto.setInfo(info);
        return visitDto;
    }
}
