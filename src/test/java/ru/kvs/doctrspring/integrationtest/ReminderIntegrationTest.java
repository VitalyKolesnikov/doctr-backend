package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.kvs.doctrspring.adapters.restapi.dto.PatientDto;
import ru.kvs.doctrspring.adapters.restapi.dto.ReminderDto;
import ru.kvs.doctrspring.adapters.restapi.dto.ErrorRepresentation;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReminderIntegrationTest extends AbstractTestBase {

    @Test
    @DisplayName("API returns all active reminders")
    void getActive() throws Exception {
        // given
        givenReminders();

        // when
        var resultActions = mockMvc.perform(get("/api/v1/reminders/"))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].patient.id", is(1100)))
                .andExpect(jsonPath("$.[0].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[0].text", is("p-1 r-1")))
                .andExpect(jsonPath("$.[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[1].patient.id", is(1100)))
                .andExpect(jsonPath("$.[1].date", is("01.09.2022")))
                .andExpect(jsonPath("$.[1].text", is("p-1 r-3")))
                .andExpect(jsonPath("$.[1].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[2].patient.id", is(1101)))
                .andExpect(jsonPath("$.[2].date", is("17.08.2022")))
                .andExpect(jsonPath("$.[2].text", is("p-2 r-1")))
                .andExpect(jsonPath("$.[2].status", is("ACTIVE")));
    }

    @Test
    @DisplayName("API returns reminder by id")
    void getById() throws Exception {
        // given
        var reminderIds = givenReminders();
        var reminderId = reminderIds.get(0);

        // when
        var resultActions = mockMvc.perform(get("/api/v1/reminders/{id}", reminderId))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$.id", comparesEqualTo(reminderId.intValue())));
    }

    @Test
    @DisplayName("API returns 404 when reminder not found by id")
    void getByIdNotFound() {
        // given
        givenReminders();

        // when
        var errorRepresentation = RestAssured.given()
                .get("/api/v1/reminders/{id}", WRONG_ID)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .body().as(ErrorRepresentation.class);

        // then
        assertThat(errorRepresentation.getMessage())
                .isEqualTo(String.format("Reminder with id [%s] not found", WRONG_ID));
    }

    @Test
    @DisplayName("API returns all reminders of patient sorted by status and date desc")
    void getForPatient() throws Exception {
        // given
        givenReminders();
        Long patientId = 1100L;

        // when
        var resultActions = mockMvc.perform(get("/api/v1/reminders/patient/{id}", patientId))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].patient.id", is(1100)))
                .andExpect(jsonPath("$.[0].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[0].text", is("p-1 r-1")))
                .andExpect(jsonPath("$.[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[1].patient.id", is(1100)))
                .andExpect(jsonPath("$.[1].date", is("01.09.2022")))
                .andExpect(jsonPath("$.[1].text", is("p-1 r-3")))
                .andExpect(jsonPath("$.[1].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[2].patient.id", is(1100)))
                .andExpect(jsonPath("$.[2].date", is("10.09.2022")))
                .andExpect(jsonPath("$.[2].text", is("p-1 r-2")))
                .andExpect(jsonPath("$.[2].status", is("NOT_ACTIVE")));
    }

    @Test
    @DisplayName("API returns active reminders count")
    void getActiveCount() throws Exception {
        // given
        givenReminders();

        // when
        var resultActions = mockMvc.perform(get("/api/v1/reminders/count/"))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", is(3)));
    }

    @Test
    @DisplayName("API creates new reminder")
    void create() throws Exception {
        // given
        var patientId = givenPatients().get(0);

        // when
        var createdReminderId = givenReminder(patientId, LocalDate.of(2022, 9, 5), "p-1 r-1");

        // then
        mockMvc.perform(get("/api/v1/reminders/{id}", createdReminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(createdReminderId.intValue())))
                .andExpect(jsonPath("$.patient.id", comparesEqualTo(patientId.intValue())))
                .andExpect(jsonPath("$.date", is("05.09.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-1")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("API updates existing reminder")
    void update() throws Exception {
        // given
        var patientId = givenPatients().get(0);
        var reminderId = givenReminder(patientId, LocalDate.of(2022, 9, 5), "p-1 r-1");

        // when
        mockMvc.perform(put("/api/v1/reminders/{id}", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testResource("/json/update-reminder-request.json")))
                .andExpect(status().isOk());

        // then
        mockMvc.perform(get("/api/v1/reminders/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(reminderId.intValue())))
                .andExpect(jsonPath("$.patient.id", comparesEqualTo(patientId.intValue())))
                .andExpect(jsonPath("$.date", is("28.10.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-1-upd")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("API sets reminder completed")
    void complete() throws Exception {
        // given
        var reminderIds = givenReminders();
        var reminderId = reminderIds.get(0);

        // when
        mockMvc.perform(patch("/api/v1/reminders/complete/{id}", reminderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        mockMvc.perform(get("/api/v1/reminders/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(reminderId.intValue())))
                .andExpect(jsonPath("$.patient.id", is(1100)))
                .andExpect(jsonPath("$.date", is("01.09.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-3")))
                .andExpect(jsonPath("$.status", is("NOT_ACTIVE")));
    }

    private List<Long> givenReminders() {
        var patientIds = givenPatients();

        var patientId_1 = patientIds.get(0);
        var patientId_2 = patientIds.get(1);

        var reminderId_1 = givenReminder(patientId_1, LocalDate.of(2022, 9, 5), "p-1 r-1");
        var reminderId_2 = givenReminder(patientId_1, LocalDate.of(2022, 9, 10), "p-1 r-2");
        setReminderCompleted(reminderId_2);
        var reminderId_3 = givenReminder(patientId_1, LocalDate.of(2022, 9, 1), "p-1 r-3");

        var reminderId_4 = givenReminder(patientId_2, LocalDate.of(2022, 8, 17), "p-2 r-1");

        return List.of(reminderId_3, reminderId_2, reminderId_1, reminderId_4);
    }

    private Long givenReminder(Long patientId, LocalDate date, String text) {
        int createdReminderId = RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(ReminderDto.builder()
                        .patientId(patientId)
                        .date(date)
                        .text(text)
                        .build()
                )
                .post("/api/v1/reminders/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        return (long) createdReminderId;
    }

    private void setReminderCompleted(Long reminderId) {
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .patch("/api/v1/reminders/complete/{id}", reminderId)
                .then()
                .assertThat()
                .statusCode(200);
    }

    private List<Long> givenPatients() {
        var patientId_1 = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "111", "p-1 info");
        var patientId_2 = givenPatient("John", "Mac", "Peterson", LocalDate.of(1985, 3, 3), "jpeterson@gmail.com", "333", "p-3 info");
        var patientId_3 = givenPatient("Mike", "Robert", "Charles", LocalDate.of(1985, 2, 2), "mcharles@gmail.com", "222", "p-2 info");

        return List.of(patientId_1, patientId_2, patientId_3);
    }

    private Long givenPatient(String firstName, String middleName, String lastName, LocalDate birthDate, String email, String phone, String info) {
        int createdPatientId = RestAssured.given()
                .contentType("application/json")
                .body(PatientDto.builder()
                        .firstName(firstName)
                        .middleName(middleName)
                        .lastName(lastName)
                        .birthDate(birthDate)
                        .email(email)
                        .phone(phone)
                        .info(info)
                        .build()
                )
                .post("/api/v1/patients/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        return (long) createdPatientId;
    }

}
