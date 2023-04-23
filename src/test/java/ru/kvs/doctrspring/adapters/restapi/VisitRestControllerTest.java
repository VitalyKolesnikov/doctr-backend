package ru.kvs.doctrspring.adapters.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kvs.doctrspring.adapters.restapi.dto.request.VisitCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ClinicDto;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.adapters.restapi.dto.response.VisitDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.VisitMapper;
import ru.kvs.doctrspring.app.VisitService;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.Visit;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.domain.ids.VisitId;
import ru.kvs.doctrspring.security.JwtTokenFilter;
import ru.kvs.doctrspring.utility.JsonHelper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VisitRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class VisitRestControllerTest {

    private static final UserId USER_ID = UserId.of("548f3c8b-4e06-45c6-b7ee-3c7db137796e");
    private static final PatientId PATIENT_ID = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
    private static final ClinicId CLINIC_ID = ClinicId.of("333fda40-6b43-4770-912c-81dc6e1ec163");
    private static final VisitId VISIT_ID = VisitId.of("6306f5f8-a00b-4d09-a652-c270f63254ad");

    private static final String REST_URL = "/api/v1/visits/";

    @MockBean
    private VisitService visitService;
    @MockBean
    private VisitMapper visitMapper;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    private MockMvc mockMvc;

    private final VisitCreateOrUpdateRequest visitCreateOrUpdateRequest = VisitCreateOrUpdateRequest.builder()
            .patientId(PatientId.of(PATIENT_ID))
            .clinicId(ClinicId.of(CLINIC_ID))
            .date(LocalDate.now())
            .cost(100)
            .percent(50)
            .child(false)
            .first(true)
            .info("test visit")
            .build();

    private final Visit visit = Visit.builder()
            .id(VisitId.of(VISIT_ID))
            .doctorId(UserId.of(USER_ID))
            .patient(Patient.builder().id(PatientId.of(PATIENT_ID)).build())
            .clinic(Clinic.builder().id(ClinicId.of(CLINIC_ID)).build())
            .date(LocalDate.now())
            .cost(100)
            .percent(50)
            .child(false)
            .first(true)
            .info("test visit")
            .build();

    private final VisitDto visitDto = VisitDto.builder()
            .id(VISIT_ID.toString())
            .clinic(ClinicDto.builder().id(CLINIC_ID.toString()).build())
            .patient(PatientDto.builder().id(PATIENT_ID.toString()).build())
            .date(LocalDate.now())
            .cost(100)
            .percent(50)
            .child(false)
            .first(true)
            .info("test visit")
            .share((int) Math.round(100 / 100.0 * 50))
            .build();

    private final List<Visit> visits = Collections.singletonList(visit);
    private final List<VisitDto> visitDtos = Collections.singletonList(visitDto);

    @Test
    @DisplayName("getAllInTimeRangeGroupByDate should return list of dated visit list dtos")
    void testGetAllInTimeRangeGroupByDate() throws Exception {
        // given
        when(visitService.getAllInTimeRangeGroupByDate(any(UserId.class), anyInt())).thenReturn(Collections.emptyMap());

        // when, then
        mockMvc.perform(get(REST_URL)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(visitService).getAllInTimeRangeGroupByDate(any(UserId.class), anyInt());
    }

    @Test
    @DisplayName("get should return visit dto for given id")
    void testGet() throws Exception {
        // given
        when(visitService.get(eq(VisitId.of(VISIT_ID)), eq(UserId.of(USER_ID)))).thenReturn(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(visitDto);

        // when, then
        mockMvc.perform(get(REST_URL + VISIT_ID)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonHelper.toJson(visitDto)));

        verify(visitService).get(eq(VisitId.of(VISIT_ID)), eq(UserId.of(USER_ID)));
        verify(visitMapper).toVisitDto(visit);
    }

    @Test
    @DisplayName("getForPatient should return list of visit dtos for given patient id")
    void testGetForPatient() throws Exception {
        // given
        when(visitService.getForPatient(eq(UserId.of(USER_ID)), eq(PatientId.of(PATIENT_ID)))).thenReturn(visits);
        when(visitMapper.toVisitDtos(visits)).thenReturn(visitDtos);

        // when, then
        mockMvc.perform(get(REST_URL + "patient/" + PATIENT_ID)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonHelper.toJson(visitDtos)));

        verify(visitService).getForPatient(eq(UserId.of(USER_ID)), eq(PatientId.of(PATIENT_ID)));
        verify(visitMapper).toVisitDtos(visits);
    }

    @Test
    @DisplayName("create should create new visit and return its dto")
    void testCreate() throws Exception {
        // given
        when(visitMapper.toVisit(eq(visitCreateOrUpdateRequest))).thenReturn(visit);
        when(visitService.create(any(), eq(UserId.of(USER_ID)), eq(PATIENT_ID), eq(CLINIC_ID))).thenReturn(visit);
        when(visitMapper.toVisitDto(visit)).thenReturn(visitDto);

        // when, then
        mockMvc.perform(post(REST_URL)
                        .contentType(APPLICATION_JSON)
                        .content(JsonHelper.toJson(visitCreateOrUpdateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(visitDto.getId()))
                .andExpect(header().string("Location", "http://localhost" + REST_URL + visit.getId().asString()));

        verify(visitMapper).toVisit(any());
        verify(visitService).create(any(), eq(UserId.of(USER_ID)), eq(PATIENT_ID), eq(CLINIC_ID));
        verify(visitMapper).toVisitDto(visit);
    }

    @Test
    @DisplayName("update should update visit and return no content")
    void testUpdate() throws Exception {
        // given
        VisitId id = VisitId.of(VISIT_ID);
        when(visitMapper.toVisit(eq(visitCreateOrUpdateRequest))).thenReturn(visit);

        // when, then
        mockMvc.perform(put(REST_URL + VISIT_ID)
                        .contentType(APPLICATION_JSON)
                        .content(JsonHelper.toJson(visitCreateOrUpdateRequest)))
                .andExpect(status().isNoContent());

        verify(visitMapper).toVisit(any());
        verify(visitService).update(any(), eq(id), eq(UserId.of(USER_ID)));
    }

    @Test
    @DisplayName("delete should delete visit and return no content")
    void testDelete() throws Exception {
        // given
        VisitId id = VisitId.of(VISIT_ID);

        // when, then
        mockMvc.perform(delete(REST_URL + VISIT_ID)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(visitService).delete(eq(id), eq(UserId.of(USER_ID)));
    }

}
