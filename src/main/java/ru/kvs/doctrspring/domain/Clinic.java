package ru.kvs.doctrspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.UserId;

@Entity
@Table(name = "clinics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Clinic extends BaseEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ClinicId id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "doctor_id"))
    private UserId doctorId;

    @Override
    protected void generateId() {
        this.id = ClinicId.newId();
    }

}
