package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitId extends BaseId {

    private VisitId(UUID value) {
        super(value);
    }

    public static VisitId newId() {
        return new VisitId(UUID.randomUUID());
    }

    @JsonCreator
    public static VisitId of(String value) {
        return new VisitId(UUID.fromString(value));
    }

    public static VisitId of(BaseId id) {
        return VisitId.of(id.asString());
    }

}
