package ru.kvs.doctrspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.model.Patient;
import ru.kvs.doctrspring.model.Status;
import ru.kvs.doctrspring.repository.PatientRepository;
import ru.kvs.doctrspring.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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
    PatientRepository patientRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        PATIENT1.setStatus(Status.ACTIVE);
    }

    @Test
    void getActive_ShouldCall_Repository() {
        service.getActive(DOCTOR_ID);

        verify(patientRepository).getActive(DOCTOR_ID);
    }

    @Test
    void get_ShouldCall_Repository() {
        service.get(PATIENT_ID, DOCTOR_ID);

        verify(patientRepository).findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
    }

    @Test
    void getSuggested_ShouldCheck_FirstLastAndMiddleNames() {
        when(patientRepository.getActive(DOCTOR_ID)).thenReturn(PATIENTS);

        List<Patient> list = service.getSuggested(DOCTOR_ID, "Paul");

        verify(patientRepository).getActive(DOCTOR_ID);
        assertThat(list, hasSize(3));
    }

    @Test
    void getSuggested_ShouldIgnoreCase() {
        when(patientRepository.getActive(DOCTOR_ID)).thenReturn(PATIENTS);

        List<Patient> list = service.getSuggested(DOCTOR_ID, "PAUL");

        verify(patientRepository).getActive(DOCTOR_ID);
        assertThat(list, hasSize(3));
    }

    @Test
    void getSuggested_ShouldReturn_EmptyList_When_NothingFound() {
        when(patientRepository.getActive(DOCTOR_ID)).thenReturn(PATIENTS);

        List<Patient> list = service.getSuggested(DOCTOR_ID, "andrew");

        verify(patientRepository).getActive(DOCTOR_ID);
        assertThat(list, hasSize(0));
    }

    @Test
    void update_ShouldThrow_When_PatientIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null, DOCTOR_ID));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrow_When_PatientNotFoundById() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.update(PATIENT1, DOCTOR_ID));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void update_ShouldSet_Doctor() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);
        when(userRepository.getOne(DOCTOR_ID)).thenReturn(DOCTOR);

        service.update(PATIENT1, DOCTOR_ID);

        USER_MATCHER.assertMatch(PATIENT1.getDoctor(), DOCTOR);
    }

    @Test
    void update_ShouldSet_Created() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);
        when(userRepository.getOne(DOCTOR_ID)).thenReturn(DOCTOR);

        service.update(UPDATED_PATIENT1, DOCTOR_ID);

        assertEquals(UPDATED_PATIENT1.getCreated(), PATIENT1.getCreated());
    }

    @Test
    void update_ShouldCall_Repository() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.update(UPDATED_PATIENT1, DOCTOR_ID);

        verify(patientRepository).findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
        verify(userRepository).getOne(DOCTOR_ID);
        verify(patientRepository).save(UPDATED_PATIENT1);
    }

    @Test
    void create_ShouldCall_Repository() {
        service.create(NEW_PATIENT_DTO, DOCTOR_ID);

        verify(userRepository).getOne(DOCTOR_ID);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void delete_ShouldDoNothing_When_StatusIsAlreadyDeleted() {
        PATIENT1.setStatus(Status.DELETED);
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.delete(PATIENT_ID, DOCTOR_ID);

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void delete_ShouldCall_Repository() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.delete(PATIENT_ID, DOCTOR_ID);

        verify(patientRepository).findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID);
        verify(patientRepository).save(PATIENT1);
    }

    @Test
    void delete_ShouldUpdate_Status() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);

        service.delete(PATIENT_ID, DOCTOR_ID);

        assertEquals(Status.DELETED, PATIENT1.getStatus());
    }

    @Test
    void delete_ShouldSet_UpdateTime() {
        when(patientRepository.findByIdAndDoctorId(PATIENT_ID, DOCTOR_ID)).thenReturn(PATIENT1);
        LocalDateTime prevUpdatedValue = PATIENT1.getUpdated();

        service.delete(PATIENT_ID, DOCTOR_ID);

        assertNotEquals(prevUpdatedValue, PATIENT1.getUpdated());
    }

}
