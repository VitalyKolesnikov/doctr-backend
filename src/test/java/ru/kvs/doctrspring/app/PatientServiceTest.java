package ru.kvs.doctrspring.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.Status;
import ru.kvs.doctrspring.domain.DoctrRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.kvs.doctrspring.data.PatientTestData.*;
import static ru.kvs.doctrspring.data.UserTestData.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @InjectMocks
    PatientService service;

    @Mock
    Clock clock;

    @Mock
    DoctrRepository doctrRepository;

    @BeforeEach
    void init() {
        PATIENT1.setStatus(Status.ACTIVE);

        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        lenient().when(clock.instant()).thenReturn(fixedClock.instant());
        lenient().when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    void getActive_ShouldCall_Repository() {
        service.getActive(DOCTOR_ID);

        verify(doctrRepository).getPatients(DOCTOR_ID);
    }

    @Test
    void get_ShouldCall_Repository() {
        service.get(PATIENT_ID, DOCTOR_ID);

        verify(doctrRepository).getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
    }

    @Test
    void getSuggested_ShouldCheck_FirstLastAndMiddleNames() {
        when(doctrRepository.getPatients(DOCTOR_ID)).thenReturn(PATIENTS);

        List<Patient> list = service.getSuggested(DOCTOR_ID, "Paul");

        verify(doctrRepository).getPatients(DOCTOR_ID);
        assertThat(list, hasSize(3));
    }

    @Test
    void getSuggested_ShouldIgnoreCase() {
        when(doctrRepository.getPatients(DOCTOR_ID)).thenReturn(PATIENTS);

        List<Patient> list = service.getSuggested(DOCTOR_ID, "PAUL");

        verify(doctrRepository).getPatients(DOCTOR_ID);
        assertThat(list, hasSize(3));
    }

    @Test
    void getSuggested_ShouldReturn_EmptyList_When_NothingFound() {
        when(doctrRepository.getPatients(DOCTOR_ID)).thenReturn(PATIENTS);

        List<Patient> list = service.getSuggested(DOCTOR_ID, "andrew");

        verify(doctrRepository).getPatients(DOCTOR_ID);
        assertThat(list, hasSize(0));
    }

    @Test
    void update_ShouldThrow_When_PatientIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null, PATIENT_ID, DOCTOR_ID));
        verify(doctrRepository, never()).savePatient(any());
    }

    @Test
    void update_ShouldThrow_When_PatientNotFoundById() {
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> service.update(PATIENT1, PATIENT_ID, DOCTOR_ID));
        verify(doctrRepository, never()).savePatient(any());
    }

    @Test
    void update_ShouldSet_Doctor() {
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.update(UPDATED_PATIENT1, PATIENT_ID, DOCTOR_ID);

        USER_MATCHER.assertMatch(PATIENT1.getDoctor(), DOCTOR);
    }

    @Test
    void update_ShouldCall_Repository() {
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.update(UPDATED_PATIENT1, PATIENT_ID, DOCTOR_ID);

        verify(doctrRepository).getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
    }

    @Test
    void create_ShouldCall_Repository() {
        service.create(NEW_PATIENT_DTO, DOCTOR_ID);

        verify(doctrRepository).getUser(DOCTOR_ID);
        verify(doctrRepository).savePatient(any(Patient.class));
    }

    @Test
    void delete_ShouldDoNothing_When_StatusIsAlreadyDeleted() {
        PATIENT1.setStatus(Status.DELETED);
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.delete(PATIENT_ID, DOCTOR_ID);

        verify(doctrRepository, never()).savePatient(any(Patient.class));
    }

    @Test
    void delete_ShouldCall_Repository() {
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.delete(PATIENT_ID, DOCTOR_ID);

        verify(doctrRepository).getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
    }

    @Test
    void delete_ShouldUpdate_Status() {
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.delete(PATIENT_ID, DOCTOR_ID);

        assertEquals(Status.DELETED, PATIENT1.getStatus());
    }

    @Test
    void delete_ShouldSet_UpdateTime() {
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);
        LocalDateTime prevUpdatedValue = PATIENT1.getUpdated();

        service.delete(PATIENT_ID, DOCTOR_ID);

        assertNotEquals(prevUpdatedValue, PATIENT1.getUpdated());
    }

}
