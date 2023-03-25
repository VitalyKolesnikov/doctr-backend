package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReminderId extends BaseId {

    private ReminderId(UUID value) {
        super(value);
    }

    public static ReminderId newId() {
        return new ReminderId(UUID.randomUUID());
    }

    @JsonCreator
    public static ReminderId of(String value) {
        return new ReminderId(UUID.fromString(value));
    }

    public static ReminderId of(BaseId id) {
        return ReminderId.of(id.asString());
    }

}
