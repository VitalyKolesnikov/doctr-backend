package ru.kvs.doctrspring.domain.ids;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleId extends BaseId {

    private RoleId(UUID value) {
        super(value);
    }

    public static RoleId newId() {
        return new RoleId(UUID.randomUUID());
    }

    @JsonCreator
    public static RoleId of(String value) {
        return new RoleId(UUID.fromString(value));
    }

    public static RoleId of(BaseId id) {
        return RoleId.of(id.asString());
    }

}
