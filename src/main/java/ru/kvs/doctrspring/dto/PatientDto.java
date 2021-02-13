package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.kvs.doctrspring.model.Patient;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDto extends PersonDto {
    private String info;

    public Patient toPatient() {
        Patient patient = new Patient();
        patient.setId(getId());
        patient.setFirstName(getFirstName());
        patient.setMiddleName(getMiddleName());
        patient.setLastName(getLastName());
        patient.setBirthDate(getBirthDate());
        patient.setEmail(getEmail());
        patient.setPhone(getPhone());
        patient.setInfo(getInfo());

        return patient;
    }
}
