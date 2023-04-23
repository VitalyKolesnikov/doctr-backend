package ru.kvs.doctrspring.adapters.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kvs.doctrspring.adapters.restapi.dto.request.PatientCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.PatientMapper;
import ru.kvs.doctrspring.app.PatientService;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.security.AuthUtil;
import ru.kvs.doctrspring.security.JwtTokenFilter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

@WebMvcTest(controllers = PatientRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatientRestControllerTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final PatientId PATIENT_ID = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");

    private final static String REST_URL = "/api/v1/patients/";

    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("getActive should return active patients for doctor")
    void testGetActive() throws Exception {
        // given
        Patient patient = Patient.builder()
                .id(PATIENT_ID)
                .doctorId(USER_ID)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();
        PatientDto patientDto = PatientDto.builder()
                .id(PATIENT_ID.asString())
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();
        when(patientService.getActive(AuthUtil.getAuthUserId())).thenReturn(List.of(patient));
        when(patientMapper.toPatientDtos(Collections.singletonList(patient))).thenReturn(List.of(patientDto));

        // when then
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(PATIENT_ID.asString()))
                .andExpect(jsonPath("$[0].firstName").value("first"))
                .andExpect(jsonPath("$[0].middleName").value("middle"))
                .andExpect(jsonPath("$[0].lastName").value("last"))
                .andExpect(jsonPath("$[0].birthDate").value("01.01.1990"))
                .andExpect(jsonPath("$[0].email").value("email"))
                .andExpect(jsonPath("$[0].phone").value("phone"))
                .andExpect(jsonPath("$[0].info").value("info"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(patientService).getActive(AuthUtil.getAuthUserId());
        verify(patientMapper).toPatientDtos(Collections.singletonList(patient));
    }

    @Test
    @DisplayName("get should return patient if found in repository")
    void testGetWhenFound() throws Exception {
        // given
        Patient patient = Patient.builder()
                .id(PATIENT_ID)
                .doctorId(USER_ID)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();
        PatientDto patientDto = PatientDto.builder()
                .id(PATIENT_ID.asString())
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();

        when(patientService.get(PATIENT_ID, AuthUtil.getAuthUserId())).thenReturn(patient);
        when(patientMapper.toPatientDto(patient)).thenReturn(patientDto);

        // when then
        mockMvc.perform(get(REST_URL + PATIENT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(PATIENT_ID.asString()))
                .andExpect(jsonPath("firstName").value("first"))
                .andExpect(jsonPath("middleName").value("middle"))
                .andExpect(jsonPath("lastName").value("last"))
                .andExpect(jsonPath("birthDate").value("01.01.1990"))
                .andExpect(jsonPath("email").value("email"))
                .andExpect(jsonPath("phone").value("phone"))
                .andExpect(jsonPath("info").value("info"))
                .andExpect(jsonPath("status").value("ACTIVE"));

        verify(patientService).get(PATIENT_ID, AuthUtil.getAuthUserId());
        verify(patientMapper).toPatientDto(patient);
    }

    @Test
    @DisplayName("get should return NOT_FOUND if patient not found in repository")
    void testGetWhenNotFound() throws Exception {
        // given
        when(patientService.get(PATIENT_ID, AuthUtil.getAuthUserId())).thenThrow(NoSuchElementException.class);

        // when then
        mockMvc.perform(get(REST_URL + PATIENT_ID))
                .andExpect(status().isNotFound());

        verify(patientService).get(PATIENT_ID, AuthUtil.getAuthUserId());
    }

    @Test
    @DisplayName("getSuggested should return suggested patients for doctor")
    void testGetSuggested() throws Exception {
        // given
        Patient patient = Patient.builder()
                .id(PATIENT_ID)
                .doctorId(USER_ID)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();
        PatientDto patientDto = PatientDto.builder()
                .id(PATIENT_ID.asString())
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();
        String part = "Mid";

        when(patientService.getSuggested(AuthUtil.getAuthUserId(), part)).thenReturn(Collections.singletonList(patient));
        when(patientMapper.toPatientDtos(Collections.singletonList(patient))).thenReturn(Collections.singletonList(patientDto));

        // when then
        mockMvc.perform(get(REST_URL + "suggest/" + part))
                .andExpect(jsonPath("$[0].id").value(PATIENT_ID.asString()))
                .andExpect(jsonPath("$[0].firstName").value("first"))
                .andExpect(jsonPath("$[0].middleName").value("middle"))
                .andExpect(jsonPath("$[0].lastName").value("last"))
                .andExpect(jsonPath("$[0].birthDate").value("01.01.1990"))
                .andExpect(jsonPath("$[0].email").value("email"))
                .andExpect(jsonPath("$[0].phone").value("phone"))
                .andExpect(jsonPath("$[0].info").value("info"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(patientService).getSuggested(AuthUtil.getAuthUserId(), part);
        verify(patientMapper).toPatientDtos(Collections.singletonList(patient));
    }

    @Test
    @DisplayName("create should create a new patient")
    void testCreate() throws Exception {
        // given
        PatientCreateOrUpdateRequest request = PatientCreateOrUpdateRequest.builder()
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .build();
        Patient patient = Patient.builder()
                .id(PATIENT_ID)
                .doctorId(USER_ID)
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();
        PatientDto patientDto = PatientDto.builder()
                .id(PATIENT_ID.asString())
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .status(ACTIVE)
                .build();

        when(patientMapper.toPatient(request)).thenReturn(patient);
        when(patientService.create(any(), eq(USER_ID))).thenReturn(patient);
        when(patientMapper.toPatientDto(patient)).thenReturn(patientDto);

        // when
        mockMvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON).content("{\"firstName\":\"name\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(PATIENT_ID.asString()))
                .andExpect(jsonPath("firstName").value("first"))
                .andExpect(jsonPath("middleName").value("middle"))
                .andExpect(jsonPath("lastName").value("last"))
                .andExpect(jsonPath("birthDate").value("01.01.1990"))
                .andExpect(jsonPath("email").value("email"))
                .andExpect(jsonPath("phone").value("phone"))
                .andExpect(jsonPath("info").value("info"))
                .andExpect(jsonPath("status").value("ACTIVE"))
                .andExpect(header().string("Location", "http://localhost" + REST_URL + PATIENT_ID));

        verify(patientService).create(any(), eq(USER_ID));
        verify(patientMapper).toPatientDto(patient);
    }

    @Test
    @DisplayName("update should update an existing patient")
    void testUpdate() throws Exception {
        // given
        PatientCreateOrUpdateRequest request = PatientCreateOrUpdateRequest.builder()
                .firstName("first")
                .middleName("middle")
                .lastName("last")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("email")
                .phone("phone")
                .info("info")
                .build();

        // when
        mockMvc.perform(put(REST_URL + PATIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated Patient Name\"}"))
                .andExpect(status().isNoContent());

        // then
        verify(patientService).update(patientMapper.toPatient(request), PATIENT_ID, USER_ID);
    }

    @Test
    @DisplayName("delete should delete an existing patient")
    void testDelete() throws Exception {
        // when
        mockMvc.perform(delete(REST_URL + PATIENT_ID))
                .andExpect(status().isNoContent());

        // then
        verify(patientService).delete(PATIENT_ID, AuthUtil.getAuthUserId());
    }

}
