package ru.kvs.doctrspring.adapters.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.kvs.doctrspring.domain.Visit;

import java.time.LocalDate;
import java.util.List;

@Data
public class DatedVisitListDto implements Comparable<DatedVisitListDto> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private final LocalDate date;
    private final List<Visit> visits;
    private final Integer totalSum;
    private final Integer totalShare;

    public DatedVisitListDto(LocalDate date, List<Visit> visits) {
        this.date = date;
        this.visits = visits;
        this.totalSum = visits.stream()
                .mapToInt(Visit::getCost)
                .sum();
        this.totalShare = visits.stream()
                .mapToInt(Visit::getShare)
                .sum();
    }

    @Override
    public int compareTo(DatedVisitListDto o) {
        return getDate().compareTo(o.getDate());
    }

}
