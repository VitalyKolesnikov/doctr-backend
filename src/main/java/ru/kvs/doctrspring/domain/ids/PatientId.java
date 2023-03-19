package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientId extends BaseId {

    private PatientId(UUID value) {
        super(value);
    }

    public static PatientId newId() {
        return new PatientId(UUID.randomUUID());
    }

    @JsonCreator
    public static PatientId of(String value) {
        return new PatientId(UUID.fromString(value));
    }

    public static PatientId of(BaseId id) {
        return PatientId.of(id.asString());
    }

}
