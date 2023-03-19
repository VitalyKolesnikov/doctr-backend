package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId extends BaseId {

    private UserId(UUID value) {
        super(value);
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    @JsonCreator
    public static UserId of(String value) {
        return new UserId(UUID.fromString(value));
    }

    public static UserId of(BaseId id) {
        return UserId.of(id.asString());
    }

}
