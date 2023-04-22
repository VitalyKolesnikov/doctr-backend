package ru.kvs.doctrspring.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.Visit;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.domain.ids.VisitId;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

@ExtendWith(MockitoExtension.class)
public class VisitServiceTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final PatientId PATIENT_ID = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
    private static final ClinicId CLINIC_ID = ClinicId.of("333fda40-6b43-4770-912c-81dc6e1ec163");
    private static final VisitId VISIT1_ID = VisitId.of("527606ba-e93c-4480-a1da-7c7c6426f945");
    private static final VisitId VISIT2_ID = VisitId.of("913f6b3e-d173-422d-9385-54f203b77aa3");

    @Mock
    private DoctrRepository doctrRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private VisitService visitService;

    @Test
    @DisplayName("getAllInTimeRangeGroupByDate should return visits in time range grouped by date for doctor")
    void testGetAllInTimeRangeGroupByDate() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate sixMonthsAgo = now.minusMonths(6);
        Visit visit1 = Visit.builder().id(VISIT1_ID).doctorId(USER_ID).date(now).build();
        Visit visit2 = Visit.builder().id(VISIT2_ID).doctorId(USER_ID).date(sixMonthsAgo).build();
        when(clock.instant()).thenReturn(Instant.now());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(doctrRepository.getVisits(USER_ID)).thenReturn(List.of(visit1, visit2));
        Map<LocalDate, List<Visit>> expectedVisits = Map.of(now, List.of(visit1));

        // when
        Map<LocalDate, List<Visit>> actualVisits = visitService.getAllInTimeRangeGroupByDate(USER_ID, 6);

        // then
        assertEquals(expectedVisits, actualVisits);
    }

    @Test
    @DisplayName("get should return visit if found in repository")
    void testGet() {
        // given
        Visit expectedVisit = Visit.builder().id(VISIT1_ID).doctorId(USER_ID).build();
        when(doctrRepository.getVisitByIdAndDoctorId(VISIT1_ID, USER_ID)).thenReturn(expectedVisit);

        // when
        Visit actualVisit = visitService.get(VISIT1_ID, USER_ID);

        // then
        verify(doctrRepository).getVisitByIdAndDoctorId(VISIT1_ID, USER_ID);
        assertEquals(expectedVisit, actualVisit);
    }

    @Test
    @DisplayName("getForPatient should return visits of patient for doctor")
    void testGetForPatient() {
        // given
        Visit visit1 = Visit.builder()
                .id(VISIT1_ID)
                .doctorId(USER_ID)
                .patient(Patient.builder()
                        .id(PATIENT_ID)
                        .doctorId(USER_ID)
                        .build())
                .build();
        Visit visit2 = Visit.builder()
                .id(VISIT2_ID)
                .doctorId(USER_ID)
                .patient(Patient.builder()
                        .id(PATIENT_ID)
                        .doctorId(USER_ID)
                        .build())
                .build();
        when(doctrRepository.getVisitsOfPatient(USER_ID, PATIENT_ID)).thenReturn(List.of(visit1, visit2));
        List<Visit> expectedVisits = List.of(visit1, visit2);

        // when
        List<Visit> actualVisits = visitService.getForPatient(USER_ID, PATIENT_ID);

        // then
        assertEquals(expectedVisits, actualVisits);
    }

    @Test
    @DisplayName("create should create a new visit and return it")
    void testCreate() {
        // given
        Visit visit = Visit.builder().id(VISIT1_ID).build();
        Visit expectedVisit = Visit.builder().id(VISIT1_ID).doctorId(USER_ID).build();
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, USER_ID))
                .thenReturn(Patient.builder()
                        .id(PATIENT_ID)
                        .doctorId(USER_ID)
                        .build());
        when(doctrRepository.getClinicByIdAndDoctorId(CLINIC_ID, USER_ID))
                .thenReturn(Clinic.builder()
                        .id(CLINIC_ID)
                        .doctorId(USER_ID)
                        .status(ACTIVE)
                        .build());
        when(doctrRepository.saveVisit(visit)).thenReturn(expectedVisit);

        // when
        Visit actualVisit = visitService.create(visit, USER_ID, PATIENT_ID, CLINIC_ID);

        // then
        assertEquals(expectedVisit, actualVisit);
    }

}
