package ru.kvs.doctrspring.data;

import ru.kvs.doctrspring.adapters.restapi.dto.VisitDto;
import ru.kvs.doctrspring.domain.Visit;

import java.time.Month;
import java.util.List;

import static java.time.LocalDateTime.of;
import static ru.kvs.doctrspring.data.ClinicTestData.CLINIC1;
import static ru.kvs.doctrspring.data.ClinicTestData.CLINIC_ID;
import static ru.kvs.doctrspring.data.PatientTestData.*;
import static ru.kvs.doctrspring.data.UserTestData.DOCTOR;

public class VisitTestData {

    public static final long VISIT_ID = 1020L;

    public static final Visit VISIT1 = Visit.builder()
            .id(VISIT_ID)
            .doctor(DOCTOR)
            .patient(PATIENT1)
            .clinic(CLINIC1)
            .created(of(2021, Month.FEBRUARY, 24, 13, 0, 0))
            .build();

    public static final Visit VISIT2 = Visit.builder()
            .id(VISIT_ID)
            .doctor(DOCTOR)
            .patient(PATIENT2)
            .build();

    public static final Visit VISIT3 = Visit.builder()
            .id(VISIT_ID)
            .doctor(DOCTOR)
            .patient(PATIENT3)
            .build();

    public static final Visit VISIT4 = Visit.builder()
            .id(VISIT_ID)
            .doctor(DOCTOR)
            .patient(PATIENT4)
            .build();

    public static final List<Visit> VISITS = List.of(VISIT1, VISIT2, VISIT3, VISIT4);

    public static final Visit UPDATED_VISIT1 = Visit.builder()
            .id(VISIT_ID)
            .build();

    public static final VisitDto VISIT_DTO = VisitDto.builder()
            .id(VISIT_ID)
            .patientId(PATIENT_ID)
            .clinicId(CLINIC_ID)
            .build();

}
