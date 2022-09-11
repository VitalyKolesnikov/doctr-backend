package ru.kvs.doctrspring.adapters.restapi.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@SuperBuilder
public class VisitCreateOrUpdateRequest {
    private Long clinicId;
    private Long patientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;
    private Integer cost;
    private Integer percent;
    private Boolean child;
    private Boolean first;
    private String info;
}
