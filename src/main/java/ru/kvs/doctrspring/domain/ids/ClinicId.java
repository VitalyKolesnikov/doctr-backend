package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClinicId extends BaseId {

    private ClinicId(UUID value) {
        super(value);
    }

    public static ClinicId newId() {
        return new ClinicId(UUID.randomUUID());
    }

    @JsonCreator
    public static ClinicId of(String value) {
        return new ClinicId(UUID.fromString(value));
    }

    public static ClinicId of(BaseId id) {
        return ClinicId.of(id.asString());
    }

}
