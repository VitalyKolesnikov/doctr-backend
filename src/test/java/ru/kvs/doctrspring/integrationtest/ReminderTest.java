package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.kvs.doctrspring.adapters.restapi.dto.request.ReminderCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ErrorRepresentation;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.ReminderId;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {
        "/sql/clearDb.sql",
        "/sql/user.sql",
        "/sql/clinics.sql",
        "/sql/patients.sql"
})
public class ReminderTest extends AbstractTestBase {

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
                .andExpect(jsonPath("$.[0].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[0].text", is("p-1 r-1")))
                .andExpect(jsonPath("$.[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[1].date", is("01.09.2022")))
                .andExpect(jsonPath("$.[1].text", is("p-1 r-3")))
                .andExpect(jsonPath("$.[1].status", is("ACTIVE")))
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
                .andExpect(jsonPath("$.id", is(reminderId.asString())))
                .andExpect(jsonPath("$.date", is("01.09.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-3")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.patient.firstName", is("Adam")))
                .andExpect(jsonPath("$.patient.middleName", is("Peter")))
                .andExpect(jsonPath("$.patient.lastName", is("Brown")))
                .andExpect(jsonPath("$.patient.birthDate", is("01.01.1985")))
                .andExpect(jsonPath("$.patient.email", is("abrown@gmail.com")))
                .andExpect(jsonPath("$.patient.phone", is("111")))
                .andExpect(jsonPath("$.patient.info", is("p-1 info")))
                .andExpect(jsonPath("$.patient.status", is("ACTIVE")));
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
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");

        // when
        var resultActions = mockMvc.perform(get("/api/v1/reminders/patient/{id}", patientId))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
                .andExpect(jsonPath("$.[0].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[0].text", is("p-1 r-1")))
                .andExpect(jsonPath("$.[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[1].patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
                .andExpect(jsonPath("$.[1].date", is("01.09.2022")))
                .andExpect(jsonPath("$.[1].text", is("p-1 r-3")))
                .andExpect(jsonPath("$.[1].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[2].patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
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
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");

        // when
        var reminderId = givenReminder(patientId, LocalDate.of(2022, 9, 5), "p-1 r-1");

        // then
        mockMvc.perform(get("/api/v1/reminders/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reminderId.asString())))
                .andExpect(jsonPath("$.patient.id", is(patientId.asString())))
                .andExpect(jsonPath("$.date", is("05.09.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-1")))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.updated", notNullValue()))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("API updates existing reminder")
    void update() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
        var reminderId = givenReminder(patientId, LocalDate.of(2022, 9, 5), "p-1 r-1");

        // when
        mockMvc.perform(put("/api/v1/reminders/{id}", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testResource("/json/update-reminder-request.json")))
                .andExpect(status().isOk());

        // then
        mockMvc.perform(get("/api/v1/reminders/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reminderId.asString())))
                .andExpect(jsonPath("$.patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
                .andExpect(jsonPath("$.date", is("28.10.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-1-upd")))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.updated", notNullValue()))
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
                .andExpect(jsonPath("$.id", is(reminderId.asString())))
                .andExpect(jsonPath("$.patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
                .andExpect(jsonPath("$.date", is("01.09.2022")))
                .andExpect(jsonPath("$.text", is("p-1 r-3")))
                .andExpect(jsonPath("$.status", is("NOT_ACTIVE")));
    }

    static List<ReminderId> givenReminders() {
        var patientIds = List.of(
                PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d"),
                PatientId.of("5d6b362f-5953-4e6c-8ae9-82f03ace7039"),
                PatientId.of("d8643baf-f50a-4248-93bc-106d5e1f1440")
        );

        var patientId_1 = patientIds.get(0);
        var patientId_2 = patientIds.get(1);

        var reminderId_1 = givenReminder(patientId_1, LocalDate.of(2022, 9, 5), "p-1 r-1");
        var reminderId_2 = givenReminder(patientId_1, LocalDate.of(2022, 9, 10), "p-1 r-2");
        setReminderCompleted(reminderId_2);
        var reminderId_3 = givenReminder(patientId_1, LocalDate.of(2022, 9, 1), "p-1 r-3");

        var reminderId_4 = givenReminder(patientId_2, LocalDate.of(2022, 8, 17), "p-2 r-1");

        return List.of(reminderId_3, reminderId_2, reminderId_1, reminderId_4);
    }

    static ReminderId givenReminder(PatientId patientId, LocalDate date, String text) {
        String createdReminderId = RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(ReminderCreateOrUpdateRequest.builder()
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

        return ReminderId.of(createdReminderId);
    }

    static void setReminderCompleted(ReminderId reminderId) {
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .patch("/api/v1/reminders/complete/{id}", reminderId.asString())
                .then()
                .assertThat()
                .statusCode(200);
    }

}
