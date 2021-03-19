package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.model.Visit;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitDto extends BaseDto {
    private Long clinicId;
    private Long patientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;
    private Integer cost;
    private Integer percent;
    private Boolean child;
    private Boolean first;
    private String info;

    public Visit toVisit() {
        Visit visit = new Visit();
        visit.setDate(getDate());
        visit.setCost(getCost());
        visit.setPercent(getPercent());
        visit.setChild(getChild());
        visit.setFirst(getFirst());
        visit.setInfo(getInfo());
        return visit;
    }
}
