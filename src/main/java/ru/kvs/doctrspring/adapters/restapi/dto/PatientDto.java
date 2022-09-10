package ru.kvs.doctrspring.adapters.restapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.Patient;

@Getter
@NoArgsConstructor
@SuperBuilder
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
