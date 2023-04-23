package ru.kvs.doctrspring.adapters.restapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kvs.doctrspring.adapters.restapi.dto.request.ReminderCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ReminderDto;
import ru.kvs.doctrspring.adapters.restapi.mapper.ReminderMapper;
import ru.kvs.doctrspring.app.ReminderService;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.domain.Reminder;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;
import ru.kvs.doctrspring.domain.ids.UserId;
import ru.kvs.doctrspring.security.JwtTokenFilter;
import ru.kvs.doctrspring.utility.JsonHelper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

@WebMvcTest(ReminderRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReminderRestControllerTest {

    private static final String REST_URL = "/api/v1/reminders/";

    @MockBean
    private ReminderService reminderService;
    @MockBean
    private ReminderMapper reminderMapper;
    @MockBean
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("getActive should return active reminders")
    void testGetActive() throws Exception {
        // given
        List<Reminder> reminders = Arrays.asList(createReminder(), createReminder());
        List<ReminderDto> reminderDtos = Arrays.asList(createReminderDto(), createReminderDto());

        when(reminderService.getActive(any(UserId.class))).thenReturn(reminders);
        when(reminderMapper.toReminderDtos(anyList())).thenReturn(reminderDtos);

        // when then
        mockMvc.perform(get(REST_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(reminderDtos.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(reminderDtos.get(1).getId()));

        verify(reminderService).getActive(any(UserId.class));
        verify(reminderMapper).toReminderDtos(reminders);
    }

    @Test
    @DisplayName("get should return reminder by id")
    void testGet() throws Exception {
        // given
        Reminder reminder = createReminder();
        ReminderDto reminderDto = createReminderDto();

        when(reminderService.get(any(ReminderId.class), any(UserId.class))).thenReturn(reminder);
        when(reminderMapper.toReminderDto(any(Reminder.class))).thenReturn(reminderDto);

        // when then
        mockMvc.perform(get(REST_URL + reminder.getId().asString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reminderDto.getId()));

        verify(reminderService).get(eq(reminder.getId()), any(UserId.class));
        verify(reminderMapper).toReminderDto(reminder);
    }

    @Test
    @DisplayName("getForPatient should return reminders for patient")
    void testGetForPatient() throws Exception {
        // given
        List<Reminder> reminders = Arrays.asList(createReminder(), createReminder());
        List<ReminderDto> reminderDtos = Arrays.asList(createReminderDto(), createReminderDto());
        PatientId patientId = PatientId.newId();

        when(reminderService.getByPatient(any(UserId.class), eq(patientId))).thenReturn(reminders);
        when(reminderMapper.toReminderDtos(anyList())).thenReturn(reminderDtos);

        // when then
        mockMvc.perform(get(REST_URL + "patient/" + patientId.asString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(reminderDtos.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(reminderDtos.get(1).getId()));

        verify(reminderService).getByPatient(any(UserId.class), eq(patientId));
        verify(reminderMapper).toReminderDtos(reminders);
    }

    @Test
    @DisplayName("getActiveCount should return active reminder count")
    void testGetActiveCount() throws Exception {
        // given
        int count = 10;
        when(reminderService.getActiveCount(any(UserId.class))).thenReturn(count);

        // when then
        mockMvc.perform(get(REST_URL + "count/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(count)));

        verify(reminderService).getActiveCount(any(UserId.class));
    }

    @Test
    @DisplayName("create should create reminder and return created reminder")
    void testCreate() throws Exception {
        // given
        ReminderCreateOrUpdateRequest request = ReminderCreateOrUpdateRequest.builder()
                .patientId(PatientId.newId())
                .date(LocalDate.now())
                .text("Take medicine")
                .build();
        Reminder reminder = createReminder();
        ReminderDto reminderDto = createReminderDto();

        when(reminderMapper.toReminder(eq(request))).thenReturn(reminder);
        when(reminderService.create(any(), eq(request.getPatientId()), any(UserId.class))).thenReturn(reminder);
        when(reminderMapper.toReminderDto(eq(reminder))).thenReturn(reminderDto);

        // when then
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reminderDto.getId()));

        verify(reminderMapper).toReminder(any());
        verify(reminderService).create(any(), eq(request.getPatientId()), any(UserId.class));
        verify(reminderMapper).toReminderDto(reminder);
    }

    @Test
    @DisplayName("update should update reminder and return updated reminder count")
    void testUpdate() throws Exception {
        // given
        ReminderCreateOrUpdateRequest request = ReminderCreateOrUpdateRequest.builder()
                .patientId(PatientId.newId())
                .date(LocalDate.now())
                .text("Take medicine")
                .build();
        Reminder reminder = createReminder();

        when(reminderMapper.toReminder(eq(request))).thenReturn(reminder);
        when(reminderService.update(any(), any(ReminderId.class), any(UserId.class))).thenReturn(1);

        // when then
        mockMvc.perform(put(REST_URL + reminder.getId().asString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(reminderMapper).toReminder(any());
        verify(reminderService).update(any(), eq(reminder.getId()), any(UserId.class));
    }

    @Test
    @DisplayName("complete should complete reminder and return completed reminder count")
    void testComplete() throws Exception {
        // given
        Reminder reminder = createReminder();
        when(reminderService.complete(eq(reminder.getId()), any(UserId.class))).thenReturn(1);

        // when then
        mockMvc.perform(patch(REST_URL + "complete/" + reminder.getId().asString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(reminderService).complete(eq(reminder.getId()), any(UserId.class));
    }

    private Reminder createReminder() {
        return Reminder.builder()
                .id(ReminderId.newId())
                .doctorId(UserId.newId())
                .patient(Patient.builder()
                        .id(PatientId.newId())
                        .build())
                .date(LocalDate.now())
                .text("Take medicine")
                .build();
    }

    private ReminderDto createReminderDto() {
        return ReminderDto.builder()
                .id(UUID.randomUUID().toString())
                .created(new Date())
                .updated(new Date())
                .status(ACTIVE)
                .patient(PatientDto.builder()
                        .id(UUID.randomUUID().toString())
                        .created(new Date())
                        .updated(new Date())
                        .status(ACTIVE)
                        .build())
                .date(LocalDate.now())
                .text("Take medicine")
                .build();
    }

}
