package ru.kvs.doctrspring.adapters.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.Status;

import java.util.Date;

import static ru.kvs.doctrspring.domain.Status.ACTIVE;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseDto {
    private String id;
    private Date created;
    private Date updated;
    private Status status;

    @JsonIgnore
    public boolean isActive() {
        return this.status == ACTIVE;
    }
}
