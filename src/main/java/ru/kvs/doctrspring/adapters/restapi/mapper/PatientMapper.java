package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import ru.kvs.doctrspring.adapters.restapi.dto.PatientDto;
import ru.kvs.doctrspring.domain.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toDto(Patient model);

    Patient toEntity(PatientDto dto);

}
