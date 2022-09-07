package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.kvs.doctrspring.dto.PatientDto;
import ru.kvs.doctrspring.dto.VisitDto;
import ru.kvs.doctrspring.rest.representation.ErrorRepresentation;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VisitIntegrationTest extends AbstractTestBase {

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
                .andExpect(jsonPath("$.id", comparesEqualTo(visitId.intValue())));
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
        Long patientId = 1100L;

        // when
        var resultActions = mockMvc.perform(get("/api/v1/visits/patient/{id}", patientId))
                .andExpect(status().isOk());

        // then
        resultActions
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].date", is("10.09.2022")))
                .andExpect(jsonPath("$.[0].patient.id", comparesEqualTo(patientId.intValue())))
                .andExpect(jsonPath("$.[1].date", is("05.09.2022")))
                .andExpect(jsonPath("$.[1].patient.id", comparesEqualTo(patientId.intValue())))
        ;
    }

    @Test
    @DisplayName("API creates new visit")
    void create() throws Exception {
        // given
        var patientId = givenPatients().get(0);

        // when
        var createdVisitId = givenVisit(patientId, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", createdVisitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(createdVisitId.intValue())))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.date", is("05.09.2022")))
                .andExpect(jsonPath("$.cost", comparesEqualTo(4000)))
                .andExpect(jsonPath("$.percent", comparesEqualTo(25)))
                .andExpect(jsonPath("$.child", is(false)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.info", is("p-1 v-1")))
                .andExpect(jsonPath("$.share", comparesEqualTo(1000)));
    }

    @Test
    @DisplayName("API updates existing visit")
    void update() throws Exception {
        // given
        var patientId = givenPatients().get(0);
        var visitId = givenVisit(patientId, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");

        // when
        mockMvc.perform(put("/api/v1/visits/{id}", visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testResource("/json/update-visit-request.json")))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", visitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", comparesEqualTo(visitId.intValue())))
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
        var patientId = givenPatients().get(0);
        var visitId = givenVisit(patientId, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");

        // when
        RestAssured.given()
                .contentType("application/json")
                .delete("/api/v1/visits/{id}", visitId)
                .then()
                .assertThat()
                .statusCode(204);

        // then
        mockMvc.perform(get("/api/v1/visits/{id}", visitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DELETED")));
    }

    private List<Long> givenVisits() {
        var patientIds = givenPatients();

        var patientId_1 = patientIds.get(0);
        var patientId_2 = patientIds.get(1);

        var visitId_1 = givenVisit(patientId_1, LocalDate.of(2022, 9, 5), 4_000, 25, false, true, "p-1 v-1");
        var visitId_2 = givenVisit(patientId_1, LocalDate.of(2022, 9, 10), 6_000, 25, false, false, "p-1 v-2");

        var visitId_3 = givenVisit(patientId_2, LocalDate.of(2022, 9, 5), 10_000, 30, true, true, "p-2 v-1");

        return List.of(visitId_1, visitId_2, visitId_3);
    }

    private Long givenVisit(Long patientId, LocalDate date, Integer cost, Integer percent, Boolean child, Boolean first, String info) {
        int createdVisitId = RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(VisitDto.builder()
                        .clinicId(1004L)
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

        return (long) createdVisitId;
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
