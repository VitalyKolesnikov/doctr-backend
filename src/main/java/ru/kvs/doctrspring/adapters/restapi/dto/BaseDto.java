package ru.kvs.doctrspring.adapters.restapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.Status;

import java.util.Date;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDto {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
}
