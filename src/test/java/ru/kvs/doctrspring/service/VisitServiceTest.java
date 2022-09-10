package ru.kvs.doctrspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.model.Visit;
import ru.kvs.doctrspring.repository.DoctrRepository;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.kvs.doctrspring.data.ClinicTestData.CLINIC1;
import static ru.kvs.doctrspring.data.ClinicTestData.CLINIC_ID;
import static ru.kvs.doctrspring.data.PatientTestData.PATIENT_ID;
import static ru.kvs.doctrspring.data.UserTestData.DOCTOR_ID;
import static ru.kvs.doctrspring.data.VisitTestData.*;

@ExtendWith(MockitoExtension.class)
public class VisitServiceTest {

    @InjectMocks
    VisitService service;

    @Mock
    Clock clock;

    @Mock
    DoctrRepository repository;

    @BeforeEach
    void init() {
        VISIT1.setStatus(Status.ACTIVE);

        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        lenient().when(clock.instant()).thenReturn(fixedClock.instant());
        lenient().when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    void getActive_ShouldCall_Repository() {
        service.getLastActive(DOCTOR_ID);

        verify(repository).getVisits(DOCTOR_ID);
    }

    @Test
    void get_ShouldCall_Repository() {
        service.get(PATIENT_ID, DOCTOR_ID);

        verify(repository).getVisitByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
    }

    @Test
    void getForPatient_ShouldCall_Repository() {
        service.getForPatient(DOCTOR_ID, PATIENT_ID);

        verify(repository).getVisitsOfPatient(DOCTOR_ID, PATIENT_ID);
    }

    @Test
    void update_ShouldThrow_When_VisitIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null, DOCTOR_ID));
        verify(repository, never()).saveVisit(any());
    }

    @Test
    void update_ShouldThrow_When_VisitNotFoundById() {
        when(repository.getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.update(VISIT_DTO, DOCTOR_ID));
        verify(repository, never()).saveVisit(any());
    }

    @Test
    void update_ShouldCall_Repository() {
        when(repository.getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID)).thenReturn(VISIT1);
        when(repository.getClinicByIdAndDoctorId(CLINIC_ID, DOCTOR_ID)).thenReturn(CLINIC1);

        service.update(VISIT_DTO, DOCTOR_ID);

        verify(repository).getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID);
        verify(repository).getClinicByIdAndDoctorId(CLINIC_ID, DOCTOR_ID);
        verify(repository).saveVisit(any(Visit.class));
    }

    @Test
    void delete_ShouldDoNothing_When_StatusIsAlreadyDeleted() {
        VISIT1.setStatus(Status.DELETED);
        when(repository.getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID)).thenReturn(VISIT1);

        service.delete(VISIT_ID, DOCTOR_ID);

        verify(repository, never()).saveVisit(any(Visit.class));
    }

    @Test
    void delete_ShouldCall_Repository() {
        when(repository.getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID)).thenReturn(VISIT1);

        service.delete(VISIT_ID, DOCTOR_ID);

        verify(repository).getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID);
        verify(repository).saveVisit(VISIT1);
    }

    @Test
    void delete_ShouldUpdate_Status() {
        when(repository.getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID)).thenReturn(VISIT1);

        service.delete(VISIT_ID, DOCTOR_ID);

        assertEquals(Status.DELETED, VISIT1.getStatus());
    }

    @Test
    void delete_ShouldSet_UpdateTime() {
        when(repository.getVisitByIdAndDoctorId(VISIT_ID, DOCTOR_ID)).thenReturn(VISIT1);
        LocalDateTime prevUpdatedValue = VISIT1.getUpdated();

        service.delete(VISIT_ID, DOCTOR_ID);

        assertNotEquals(prevUpdatedValue, VISIT1.getUpdated());
    }

}
