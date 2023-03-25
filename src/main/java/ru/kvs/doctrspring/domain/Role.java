package ru.kvs.doctrspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.kvs.doctrspring.domain.ids.RoleId;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private RoleId id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    protected void generateId() {
        this.id = RoleId.newId();
    }

}
