package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kvs.doctrspring.adapters.restapi.dto.request.VisitCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.VisitDto;
import ru.kvs.doctrspring.domain.Visit;
import ru.kvs.doctrspring.domain.ids.VisitId;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, ClinicMapper.class})
public interface VisitMapper {

    VisitDto toVisitDto(Visit Visit);

    List<VisitDto> toVisitDtos(List<Visit> Visits);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "clinic", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Visit toVisit(VisitCreateOrUpdateRequest VisitCreateOrUpdateRequest);

    default String map(VisitId id) {
        return id.asString();
    }

}
