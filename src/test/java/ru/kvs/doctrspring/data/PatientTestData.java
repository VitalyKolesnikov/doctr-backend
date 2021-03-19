package ru.kvs.doctrspring.data;

import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.model.Patient;

import java.time.Month;
import java.util.List;

import static java.time.LocalDateTime.of;
import static ru.kvs.doctrspring.data.UserTestData.DOCTOR;

public class PatientTestData {

    public static final long PATIENT_ID = 1006L;

    public static final Patient PATIENT1 = Patient.builder()
            .id(PATIENT_ID)
            .lastName("Smith")
            .firstName("John")
            .middleName("Paul")
            .doctor(DOCTOR)
            .created(of(2021, Month.FEBRUARY, 24, 13, 0, 0))
            .build();

    public static final Patient PATIENT2 = Patient.builder()
            .lastName("Saar")
            .firstName("Edvin")
            .middleName("Var Der")
            .build();

    public static final Patient PATIENT3 = Patient.builder()
            .lastName("McCartney")
            .firstName("Paul")
            .middleName("John")
            .build();

    public static final Patient PATIENT4 = Patient.builder()
            .lastName("Paul")
            .firstName("Mike")
            .middleName("Dr.")
            .build();

    public static final List<Patient> PATIENTS = List.of(PATIENT1, PATIENT2, PATIENT3, PATIENT4);

    public static final Patient UPDATED_PATIENT1 = Patient.builder()
            .id(PATIENT_ID)
            .lastName("Smith")
            .firstName("John")
            .middleName("Paul")
            .info("lorem ipsum")
            .build();

    public static final PatientDto NEW_PATIENT_DTO = PatientDto.builder()
            .lastName("Edwards")
            .firstName("Jonathan")
            .middleName("Lee")
            .build();

}
