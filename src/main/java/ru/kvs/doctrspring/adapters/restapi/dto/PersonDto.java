package ru.kvs.doctrspring.adapters.restapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDto extends BaseDto {
    private String firstName;
    private String middleName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private String email;
    private String phone;
}
