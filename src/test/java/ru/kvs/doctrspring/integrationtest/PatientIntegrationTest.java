package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvs.doctrspring.adapters.restapi.dto.PatientDto;
import ru.kvs.doctrspring.domain.Patient;
import ru.kvs.doctrspring.adapters.restapi.dto.ErrorRepresentation;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;
import static ru.kvs.doctrspring.domain.Status.DELETED;

public class PatientIntegrationTest extends AbstractTestBase {

    @Test
    @DisplayName("API returns all active patients of current user sorted by lastname")
    void getAll() {
        // given
        givenPatients();

        // when
        var patients = RestAssured.given()
                .get("/api/v1/patients/")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Patient>>() {
                });

        // then
        assertThat(patients)
                .hasSize(3)
                .extracting("lastName")
                .containsExactly("Brown", "Charles", "Peterson");
    }

    @Test
    @DisplayName("API returns patient by id")
    void getById() {
        // given
        var patientIds = givenPatients();
        var patientId = patientIds.get(0);

        // when
        var patient = RestAssured.given()
                .get("/api/v1/patients/{id}", patientId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<Patient>() {
                });

        // then
        assertThat(patient.getId()).isEqualTo(patientId);
    }

    @Test
    @DisplayName("API returns patients by substring matching last/middle/first name")
    void getSuggested() {
        // given
        givenPatients();
        var query = "peter";

        // when
        var patients = RestAssured.given()
                .get("/api/v1/patients/suggest/{part}", query)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Patient>>() {
                });

        // then
        assertThat(patients).hasSize(2);
        assertThat(patients).extracting("lastName").containsExactly("Brown", "Peterson");
        assertThat(patients).extracting("middleName").containsExactly("Peter", "Mac");
    }

    @Test
    @DisplayName("API returns 404 when patient not found by id")
    void getByIdNotFound() {
        // given
        givenPatients();

        // when
        var errorRepresentation = RestAssured.given()
                .get("/api/v1/patients/{id}", WRONG_ID)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .body().as(ErrorRepresentation.class);

        // then
        assertThat(errorRepresentation.getMessage())
                .isEqualTo(String.format("Patient with id [%s] not found", WRONG_ID));
    }

    @Test
    @DisplayName("API creates new patient")
    void create() {
        // when
        var createdPatientId = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "111", "p-1 info");

        // then
        var patient = RestAssured.given()
                .get("/api/v1/patients/{id}", createdPatientId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<Patient>() {
                });

        assertThat(patient.getId()).isEqualTo(createdPatientId);
        assertThat(patient.getFirstName()).isEqualTo("Adam");
        assertThat(patient.getMiddleName()).isEqualTo("Peter");
        assertThat(patient.getLastName()).isEqualTo("Brown");
        assertThat(patient.getBirthDate()).isEqualTo(LocalDate.of(1985, 1, 1));
        assertThat(patient.getEmail()).isEqualTo("abrown@gmail.com");
        assertThat(patient.getPhone()).isEqualTo("111");
        assertThat(patient.getInfo()).isEqualTo("p-1 info");
        assertThat(patient.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("API updates existing patient")
    void update() {
        // given
        var patientId = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "111", "p-1 info");

        // when
        RestAssured.given()
                .contentType("application/json")
                .body(Patient.builder()
                        .firstName("Adam-upd")
                        .middleName("Peter-upd")
                        .lastName("Brown-upd")
                        .birthDate(LocalDate.of(1984, 12, 11))
                        .email("abrown-upd@gmail.com")
                        .phone("111-222")
                        .info("p-1 info-upd")
                        .build()
                )
                .put("/api/v1/patients/{id}", patientId)
                .then()
                .assertThat()
                .statusCode(204);

        // then
        var patient = RestAssured.given()
                .get("/api/v1/patients/{id}", patientId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<Patient>() {
                });

        assertThat(patient.getId()).isEqualTo(patientId);
        assertThat(patient.getFirstName()).isEqualTo("Adam-upd");
        assertThat(patient.getMiddleName()).isEqualTo("Peter-upd");
        assertThat(patient.getLastName()).isEqualTo("Brown-upd");
        assertThat(patient.getBirthDate()).isEqualTo(LocalDate.of(1984, 12, 11));
        assertThat(patient.getEmail()).isEqualTo("abrown-upd@gmail.com");
        assertThat(patient.getPhone()).isEqualTo("111-222");
        assertThat(patient.getInfo()).isEqualTo("p-1 info-upd");
        assertThat(patient.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("API soft-deletes existing patient")
    void softDelete() {
        // given
        var patientId = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "111", "p-1 info");

        // when
        RestAssured.given()
                .contentType("application/json")
                .delete("/api/v1/patients/{id}", patientId)
                .then()
                .assertThat()
                .statusCode(204);

        // then
        var patient = RestAssured.given()
                .get("/api/v1/patients/{id}", patientId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<Patient>() {
                });

        assertThat(patient.getStatus()).isEqualTo(DELETED);
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
