package ru.kvs.doctrspring.adapters.restapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@SuperBuilder
public class ClinicDto extends BaseDto {
    private String name;
    private String phone;
    private String address;
}
