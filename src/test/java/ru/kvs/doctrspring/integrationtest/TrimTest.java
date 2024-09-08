package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.domain.ids.PatientId;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;
import static ru.kvs.doctrspring.integrationtest.PatientTest.givenPatient;
import static ru.kvs.doctrspring.integrationtest.ReminderTest.givenReminder;
import static ru.kvs.doctrspring.integrationtest.VisitTest.givenVisit;

@Sql(value = {
        "/sql/clearDb.sql",
        "/sql/user.sql",
        "/sql/clinics.sql",
        "/sql/patients.sql"
})
public class TrimTest extends AbstractTestBase {

    @Test
    @DisplayName("All patient data should be trimmed when created")
    void createPatientTrimmed() {
        // when
        var createdPatientId = givenPatient(" Adam ", " Peter ", " Brown ",
                LocalDate.of(1985, 1, 1), " abrown@gmail.com ", " +7(915)333-22-11 ", " p-1 info ");

        // then
        var patientDto = RestAssured.given()
                .get("/api/v1/patients/{id}", createdPatientId.asString())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PatientDto>() {
                });

        assertThat(patientDto.getFirstName()).isEqualTo("Adam");
        assertThat(patientDto.getMiddleName()).isEqualTo("Peter");
        assertThat(patientDto.getLastName()).isEqualTo("Brown");
        assertThat(patientDto.getEmail()).isEqualTo("abrown@gmail.com");
        assertThat(patientDto.getPhone()).isEqualTo("+7(915)333-22-11");
        assertThat(patientDto.getInfo()).isEqualTo("p-1 info");
    }

    @Test
    @DisplayName("All patient data should be trimmed when updated")
    void updatePatientTrimmed() {
        // given
        var patientId = givenPatient("Adam", "Peter", "Brown",
                LocalDate.of(1985, 1, 1), "abrown@gmail.com", "+7(915)333-22-11", "p-1 info");

        // when
        RestAssured.given()
                .contentType("application/json")
                .body(PatientDto.builder()
                        .firstName(" Adam-upd ")
                        .middleName(" Peter-upd ")
                        .lastName(" Brown-upd ")
                        .birthDate(LocalDate.of(1984, 12, 11))
                        .email(" abrown-upd@gmail.com ")
                        .phone(" 111-222 ")
                        .info(" p-1 info-upd ")
                        .build()
                )
                .put("/api/v1/patients/{id}", patientId.asString())
                .then()
                .assertThat()
                .statusCode(204);

        // then
        var patientDto = RestAssured.given()
                .get("/api/v1/patients/{id}", patientId.asString())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PatientDto>() {
                });

        assertThat(patientDto.getFirstName()).isEqualTo("Adam-upd");
        assertThat(patientDto.getMiddleName()).isEqualTo("Peter-upd");
        assertThat(patientDto.getLastName()).isEqualTo("Brown-upd");
        assertThat(patientDto.getEmail()).isEqualTo("abrown-upd@gmail.com");
        assertThat(patientDto.getPhone()).isEqualTo("111-222");
        assertThat(patientDto.getInfo()).isEqualTo("p-1 info-upd");
    }

    @Test
    @DisplayName("All visit data should be trimmed when created")
    void createVisitTrimmed() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");

        // when
        var createdVisitId = givenVisit(patientId, LocalDate.of(2022, 9, 5),
                4_000, 25, false, true, " p-1 v-1 ");

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", createdVisitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info", is("p-1 v-1")));
    }

    @Test
    @DisplayName("All visit data should be trimmed when updated")
    void updateVisitTrimmed() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
        var visitId = givenVisit(patientId, LocalDate.of(2022, 9, 5),
                4_000, 25, false, true, "p-1 v-1");

        // when
        mockMvc.perform(put("/api/v1/visits/{id}", visitId.asString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testResource("/json/update-visit-request.json")))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", visitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info", is("p-1 v-1-upd")));
    }

    @Test
    @DisplayName("All reminder data should be trimmed when created")
    void createReminderTrimmed() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");

        // when
        var reminderId = givenReminder(patientId, LocalDate.of(2022, 9, 5), " p-1 r-1 ");

        // then
        mockMvc.perform(get("/api/v1/reminders/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("p-1 r-1")));
    }

    @Test
    @DisplayName("All reminder data should be trimmed when updated")
    void updateReminderTrimmed() throws Exception {
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

                .andExpect(jsonPath("$.text", is("p-1 r-1-upd")));
    }

}
