package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.kvs.doctrspring.adapters.restapi.dto.request.VisitCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ErrorRepresentation;
import ru.kvs.doctrspring.domain.ids.ClinicId;
import ru.kvs.doctrspring.domain.ids.PatientId;
import ru.kvs.doctrspring.domain.ids.VisitId;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {
        "/sql/clearDb.sql",
        "/sql/user.sql",
        "/sql/clinics.sql",
        "/sql/patients.sql"
})
public class VisitTest extends AbstractTestBase {

    @Test
    @DisplayName("API returns all visits of last six months grouped by date")
    void getAllGroupByDate() throws Exception {
        // given
        givenVisits();

        // when
        var resultActions = mockMvc.perform(get("/api/v1/visits/"))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].date", is("10.09.2022")))
                .andExpect(jsonPath("$.[0].visits", hasSize(1)))
                .andExpect(jsonPath("$.[0].totalSum", is(6_000)))
                .andExpect(jsonPath("$.[0].totalShare", is(1500)))
                .andExpect(jsonPath("$.[1].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[1].visits", hasSize(2)))
                .andExpect(jsonPath("$.[1].totalSum", is(14_000)))
                .andExpect(jsonPath("$.[1].totalShare", is(4_000)));
    }

    @Test
    @DisplayName("API returns visit by id")
    void getById() throws Exception {
        // given
        var visitIds = givenVisits();
        var visitId = visitIds.get(0);

        // when
        var resultActions = mockMvc.perform(get("/api/v1/visits/{id}", visitId))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$.id", is(visitId.asString())))
                .andExpect(jsonPath("$.date", is("05.09.2022")))
                .andExpect(jsonPath("$.cost", is(4000)))
                .andExpect(jsonPath("$.percent", is(25)))
                .andExpect(jsonPath("$.child", is(false)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.info", is("p-1 v-1")))
                .andExpect(jsonPath("$.share", is(1000)))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
                .andExpect(jsonPath("$.patient.firstName", is("Adam")))
                .andExpect(jsonPath("$.patient.middleName", is("Peter")))
                .andExpect(jsonPath("$.patient.lastName", is("Brown")))
                .andExpect(jsonPath("$.patient.birthDate", is("01.01.1985")))
                .andExpect(jsonPath("$.patient.email", is("abrown@gmail.com")))
                .andExpect(jsonPath("$.patient.phone", is("111")))
                .andExpect(jsonPath("$.patient.info", is("p-1 info")))
                .andExpect(jsonPath("$.patient.status", is("ACTIVE")))
                .andExpect(jsonPath("$.clinic.id", is("8b8dd071-0a7c-4262-9389-44a814287ca2")))
                .andExpect(jsonPath("$.clinic.name", is("Clinic1")))
                .andExpect(jsonPath("$.clinic.phone", is("+7(499)111-1111")))
                .andExpect(jsonPath("$.clinic.address", is("Moscow, Lenina 1")))
                .andExpect(jsonPath("$.clinic.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("API returns 404 when visit not found by id")
    void getByIdNotFound() {
        // given
        givenVisits();

        // when
        var errorRepresentation = RestAssured.given()
                .get("/api/v1/visits/{id}", WRONG_ID)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .body().as(ErrorRepresentation.class);

        // then
        assertThat(errorRepresentation.getMessage())
                .isEqualTo(String.format("Visit with id [%s] not found", WRONG_ID));
    }

    @Test
    @DisplayName("API returns all visits of patient sorted by visit date desc")
    void getForPatient() throws Exception {
        // given
        givenVisits();
        var patientId = "0a83aef4-b000-407b-905c-8ded6ff00a3d";

        // when
        var resultActions = mockMvc.perform(get("/api/v1/visits/patient/{id}", patientId))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].date", is("10.09.2022")))
                .andExpect(jsonPath("$.[0].patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")))
                .andExpect(jsonPath("$.[1].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[1].patient.id", is("0a83aef4-b000-407b-905c-8ded6ff00a3d")));
    }

    @Test
    @DisplayName("API creates new visit")
    void create() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");

        // when
        var createdVisitId = givenVisit(patientId, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", createdVisitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdVisitId.asString())))
                .andExpect(jsonPath("$.date", is("05.09.2022")))
                .andExpect(jsonPath("$.cost", comparesEqualTo(4000)))
                .andExpect(jsonPath("$.percent", comparesEqualTo(25)))
                .andExpect(jsonPath("$.child", is(false)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.info", is("p-1 v-1")))
                .andExpect(jsonPath("$.share", comparesEqualTo(1000)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.updated", notNullValue()))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("API updates existing visit")
    void update() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
        var visitId = givenVisit(patientId, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");

        // when
        mockMvc.perform(put("/api/v1/visits/{id}", visitId.asString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testResource("/json/update-visit-request.json")))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", visitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(visitId.asString())))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.date", is("12.08.2022")))
                .andExpect(jsonPath("$.cost", comparesEqualTo(3000)))
                .andExpect(jsonPath("$.percent", comparesEqualTo(30)))
                .andExpect(jsonPath("$.child", is(true)))
                .andExpect(jsonPath("$.first", is(false)))
                .andExpect(jsonPath("$.info", is("p-1 v-1-upd")))
                .andExpect(jsonPath("$.share", comparesEqualTo(900)));
    }

    @Test
    @DisplayName("API soft-deletes existing visit")
    void softDelete() throws Exception {
        // given
        var patientId = PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d");
        var visitId = givenVisit(patientId, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");

        // when
        RestAssured.given()
                .contentType("application/json")
                .delete("/api/v1/visits/{id}", visitId.asString())
                .then()
                .assertThat()
                .statusCode(204);

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", visitId.asString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DELETED")));
    }

    static List<VisitId> givenVisits() {
        var patientIds = List.of(
                PatientId.of("0a83aef4-b000-407b-905c-8ded6ff00a3d"),
                PatientId.of("5d6b362f-5953-4e6c-8ae9-82f03ace7039"),
                PatientId.of("d8643baf-f50a-4248-93bc-106d5e1f1440")
        );

        var patientId_1 = patientIds.get(0);
        var patientId_2 = patientIds.get(1);

        var visitId_1 = givenVisit(patientId_1, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");
        var visitId_2 = givenVisit(patientId_1, LocalDate.of(2022, 9, 10), 6_000, 25, false, false, "p-1 v-2");

        var visitId_3 = givenVisit(patientId_2, LocalDate.of(2022, 9, 5), 10_000, 30, true, true, "p-2 v-1");

        return List.of(visitId_1, visitId_2, visitId_3);
    }

    static VisitId givenVisit(PatientId patientId, LocalDate date, Integer cost, Integer percent, Boolean child, Boolean first, String info) {
        String createdVisitId = RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(VisitCreateOrUpdateRequest.builder()
                        .clinicId(ClinicId.of("8b8dd071-0a7c-4262-9389-44a814287ca2"))
                        .patientId(patientId)
                        .date(date)
                        .cost(cost)
                        .percent(percent)
                        .child(child)
                        .first(first)
                        .info(info)
                        .build()
                )
                .post("/api/v1/visits/")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        return VisitId.of(createdVisitId);
    }

}
