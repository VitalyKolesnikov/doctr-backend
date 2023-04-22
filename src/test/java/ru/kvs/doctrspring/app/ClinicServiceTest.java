package ru.kvs.doctrspring.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.DoctrRepository;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.UserId;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;
import static ru.kvs.doctrspring.domain.Status.NOT_ACTIVE;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final ClinicId CLINIC1_ID = ClinicId.of("333fda40-6b43-4770-912c-81dc6e1ec163");
    private static final ClinicId CLINIC2_ID = ClinicId.of("41659d74-67ac-4712-9fd3-055601eba3ab");

    @Mock
    private DoctrRepository doctrRepository;
    @InjectMocks
    private ClinicService service;

    @Test
    @DisplayName("getAll should return active clinics for doctor")
    void testGetAll() {
        // given
        List<Clinic> expectedClinics = List.of(
                Clinic.builder().id(CLINIC1_ID).doctorId(USER_ID).status(ACTIVE).build(),
                Clinic.builder().id(CLINIC2_ID).doctorId(USER_ID).status(NOT_ACTIVE).build()
        );
        when(doctrRepository.getClinicsByDoctorId(USER_ID)).thenReturn(expectedClinics);

        // when
        List<Clinic> actualClinics = service.getAll(USER_ID);

        // then
        verify(doctrRepository).getClinicsByDoctorId(USER_ID);
        assertEquals(List.of(expectedClinics.get(0)), actualClinics);
    }

    @Test
    @DisplayName("get should return clinic if found in repository")
    void testGetWhenFound() {
        // given
        Clinic expectedClinic = Clinic.builder().id(CLINIC1_ID).doctorId(USER_ID).build();
        when(doctrRepository.getClinicByIdAndDoctorId(CLINIC1_ID, USER_ID)).thenReturn(expectedClinic);

        // when
        Clinic actualClinic = service.get(CLINIC1_ID, USER_ID);

        // then
        verify(doctrRepository).getClinicByIdAndDoctorId(CLINIC1_ID, USER_ID);
        assertEquals(expectedClinic, actualClinic);
    }

}
