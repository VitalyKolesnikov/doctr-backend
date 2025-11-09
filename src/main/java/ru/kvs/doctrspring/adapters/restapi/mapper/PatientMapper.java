package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kvs.doctrspring.adapters.restapi.dto.request.PatientCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.ids.PatientId;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toPatientDto(Patient patient);

    List<PatientDto> toPatientDtos(List<Patient> patients);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Patient toPatient(PatientCreateOrUpdateRequest patientCreateOrUpdateRequest);

    default String map(PatientId id) {
        return id.asString();
    }

}
