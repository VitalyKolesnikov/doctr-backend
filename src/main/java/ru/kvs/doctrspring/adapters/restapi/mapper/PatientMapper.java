package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import ru.kvs.doctrspring.adapters.restapi.dto.request.PatientCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.ids.PatientId;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toPatientDto(Patient patient);

    List<PatientDto> toPatientDtos(List<Patient> patients);

    Patient toPatient(PatientCreateOrUpdateRequest patientCreateOrUpdateRequest);

    default String map(PatientId id) {
        return id.asString();
    }

}
