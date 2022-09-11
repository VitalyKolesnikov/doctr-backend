package ru.kvs.doctrspring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder
public abstract class Person extends BaseEntity {

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "middle_name")
    protected String middleName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    protected LocalDate birthDate;

    @Column(name = "email")
    protected String email;

    @Column(name = "phone")
    protected String phone;

}
