package ru.kvs.doctrspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.HasId;
import ru.kvs.doctrspring.model.Status;

import java.util.Date;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDto implements HasId {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
}
