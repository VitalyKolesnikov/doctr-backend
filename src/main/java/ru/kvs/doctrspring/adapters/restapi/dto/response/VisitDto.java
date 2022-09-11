package ru.kvs.doctrspring.adapters.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@SuperBuilder
public class VisitDto extends BaseDto {
    private ClinicDto clinic;
    private PatientDto patient;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;
    private Integer cost;
    private Integer percent;
    private Boolean child;
    private Boolean first;
    private String info;
    private int share;
}
