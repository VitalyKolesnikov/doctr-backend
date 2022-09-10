package ru.kvs.doctrspring.data;

import ru.kvs.doctrspring.domain.Clinic;

public class ClinicTestData {

    public static final long CLINIC_ID = 1030L;

    public static final Clinic CLINIC1 = Clinic.builder()
            .id(CLINIC_ID)
            .build();

}
