package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.kvs.doctrspring.model.Status;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDto {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
}
