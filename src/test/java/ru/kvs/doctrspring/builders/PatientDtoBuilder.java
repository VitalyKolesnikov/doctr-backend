package ru.kvs.doctrspring.builders;

import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.model.Status;

import java.time.LocalDate;
import java.util.Date;

public final class PatientDtoBuilder {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
    private String info;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;

    private PatientDtoBuilder() {
    }

    public static PatientDtoBuilder aPatientDto() {
        return new PatientDtoBuilder();
    }

    public PatientDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PatientDtoBuilder created(Date created) {
        this.created = created;
        return this;
    }

    public PatientDtoBuilder updated(Date updated) {
        this.updated = updated;
        return this;
    }

    public PatientDtoBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public PatientDtoBuilder info(String info) {
        this.info = info;
        return this;
    }

    public PatientDtoBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PatientDtoBuilder middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public PatientDtoBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PatientDtoBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PatientDtoBuilder email(String email) {
        this.email = email;
        return this;
    }

    public PatientDtoBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public PatientDto build() {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(id);
        patientDto.setCreated(created);
        patientDto.setUpdated(updated);
        patientDto.setStatus(status);
        patientDto.setInfo(info);
        patientDto.setFirstName(firstName);
        patientDto.setMiddleName(middleName);
        patientDto.setLastName(lastName);
        patientDto.setBirthDate(birthDate);
        patientDto.setEmail(email);
        patientDto.setPhone(phone);
        return patientDto;
    }
}
