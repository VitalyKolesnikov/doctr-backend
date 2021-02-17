package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.kvs.doctrspring.model.Visit;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitDto extends BaseDto {
    private Long clinicId;
    private Long patientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;
    private Integer cost;
    private String info;

    public Visit toVisit() {
        Visit visit = new Visit();
        visit.setDate(getDate());
        visit.setCost(getCost());
        visit.setInfo(getInfo());
        return visit;
    }
}
