package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ClinicDto;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.ids.ClinicId;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    ClinicDto toClinicDto(Clinic clinic);

    List<ClinicDto> toClinicDtos(List<Clinic> clinics);

    default String map(ClinicId id) {
        return id.asString();
    }

}
