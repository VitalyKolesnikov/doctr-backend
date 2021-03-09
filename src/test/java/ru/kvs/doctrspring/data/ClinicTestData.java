package ru.kvs.doctrspring.data;

import ru.kvs.doctrspring.model.Clinic;

import static ru.kvs.doctrspring.builders.ClinicBuilder.aClinic;

public class ClinicTestData {

    public static final long CLINIC_ID = 1030L;

    public static final Clinic CLINIC1 = aClinic()
            .id(CLINIC_ID)
            .build();

}
