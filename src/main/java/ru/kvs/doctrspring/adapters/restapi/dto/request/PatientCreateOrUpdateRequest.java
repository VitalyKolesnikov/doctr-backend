package ru.kvs.doctrspring.adapters.restapi.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@SuperBuilder
public class PatientCreateOrUpdateRequest extends PersonCreateOrUpdateRequest {
    private String info;
}
