package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseId implements Serializable {

    protected UUID value;

    @JsonValue
    public String asString() {
        return this.value.toString();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public UUID value() {
        return this.value;
    }

}
