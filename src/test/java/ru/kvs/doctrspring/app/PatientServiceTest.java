package ru.kvs.doctrspring.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final PatientId PATIENT1_ID = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
    private static final PatientId PATIENT2_ID = PatientId.of("52203076-0211-4e9e-a903-335d3026dcdf");

    @Mock
    private DoctrRepository doctrRepository;
    @InjectMocks
    private PatientService service;

    @Test
    @DisplayName("getActive should return active patients for doctor")
    void testGetActive() {
        // given
        List<Patient> expectedPatients = List.of(
                Patient.builder().id(PATIENT1_ID).doctorId(USER_ID).status(ACTIVE).build()
        );

        when(doctrRepository.getPatients(USER_ID)).thenReturn(expectedPatients);

        // when
        List<Patient> actualPatients = service.getActive(USER_ID);

        // then
        verify(doctrRepository).getPatients(USER_ID);
        assertEquals(List.of(expectedPatients.get(0)), actualPatients);
    }

    @Test
    @DisplayName("get should return patient if found in repository")
    void testGetWhenFound() {
        // given
        Patient expectedPatient = Patient.builder().id(PATIENT1_ID).doctorId(USER_ID).build();
        when(doctrRepository.getPatientByIdAndDoctorId(PATIENT1_ID, USER_ID)).thenReturn(expectedPatient);

        // when
        Patient actualPatient = service.get(PATIENT1_ID, USER_ID);

        // then
        verify(doctrRepository).getPatientByIdAndDoctorId(PATIENT1_ID, USER_ID);
        assertEquals(expectedPatient, actualPatient);
    }

    @Test
    @DisplayName("getSuggested should return patients that match part of their name")
    void testGetSuggested() {
        // given
        List<Patient> expectedPatients = List.of(
                Patient.builder().id(PATIENT1_ID).doctorId(USER_ID).lastName("Johnson").firstName("John").middleName("Edward").build(),
                Patient.builder().id(PATIENT2_ID).doctorId(USER_ID).lastName("Doe").firstName("Jane").middleName("Elizabeth").build()
        );
        when(doctrRepository.getPatients(USER_ID)).thenReturn(expectedPatients);

        // when
        List<Patient> actualPatients = service.getSuggested(USER_ID, "Jo");

        // then
        verify(doctrRepository).getPatients(USER_ID);
        assertEquals(List.of(expectedPatients.get(0)), actualPatients);
    }

    @Test
    @DisplayName("create should create new patient with given data for doctor")
    void testCreate() {
        // given
        Patient expectedPatient = Patient.builder().id(PATIENT1_ID).doctorId(USER_ID).lastName("Johnson").firstName("John").middleName("Edward").build();
        when(doctrRepository.savePatient(expectedPatient)).thenReturn(expectedPatient);

        // when
        Patient actualPatient = service.create(expectedPatient, USER_ID);

        // then
        verify(doctrRepository).savePatient(expectedPatient);
        assertEquals(expectedPatient, actualPatient);
    }

}
