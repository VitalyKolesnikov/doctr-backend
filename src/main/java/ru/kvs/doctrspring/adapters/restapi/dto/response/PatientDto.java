package ru.kvs.doctrspring.adapters.restapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@SuperBuilder
public class PatientDto extends PersonDto {
    private String info;
}
