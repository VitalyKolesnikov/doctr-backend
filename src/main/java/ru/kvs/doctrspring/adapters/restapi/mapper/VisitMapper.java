package ru.kvs.doctrspring.adapters.restapi.mapper;

import org.mapstruct.Mapper;
import ru.kvs.doctrspring.adapters.restapi.dto.request.VisitCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.VisitDto;
import ru.kvs.doctrspring.domain.Visit;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, ClinicMapper.class})
public interface VisitMapper {

    VisitDto toVisitDto(Visit Visit);

    List<VisitDto> toVisitDtos(List<Visit> Visits);

    Visit toVisit(VisitCreateOrUpdateRequest VisitCreateOrUpdateRequest);

}
