package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ru.kvs.doctrspring.adapters.restapi.dto.request.PatientCreateOrUpdateRequest;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ErrorRepresentation;
import ru.kvs.doctrspring.adapters.restapi.dto.response.PatientDto;
import ru.kvs.doctrspring.domain.ids.PatientId;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;
import static ru.kvs.doctrspring.domain.Status.DELETED;

@Sql(value = {
        "/sql/clearDb.sql",
        "/sql/user.sql",
        "/sql/clinics.sql"
})
public class PatientTest extends AbstractTestBase {

    @Test
    @DisplayName("API returns all active patients of current user sorted by lastname")
    void getAll() {
        // given
        givenPatients();

        // when
        var patientDtos = RestAssured.given()
                .get("/api/v1/patients/")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<PatientDto>>() {
                });

        // then
        assertThat(patientDtos)
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
        var patientDto = RestAssured.given()
                .get("/api/v1/patients/{id}", patientId.asString())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PatientDto>() {
                });

        // then
        assertThat(patientDto.getId()).isEqualTo(patientId.asString());
        assertThat(patientDto.getFirstName()).isEqualTo("Adam");
        assertThat(patientDto.getMiddleName()).isEqualTo("Peter");
        assertThat(patientDto.getLastName()).isEqualTo("Brown");
        assertThat(patientDto.getBirthDate()).isEqualTo(LocalDate.of(1985, 1, 1));
        assertThat(patientDto.getEmail()).isEqualTo("abrown@gmail.com");
        assertThat(patientDto.getPhone()).isEqualTo("+7(915)333-22-11");
        assertThat(patientDto.getInfo()).isEqualTo("p-1 info");
        assertThat(patientDto.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("API returns patients by substring matching last/middle/first name")
    void getSuggested() {
        // given
        givenPatients();
        var query = "peter";

        // when
        var patientDtos = RestAssured.given()
                .get("/api/v1/patients/suggest/{part}", query)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<PatientDto>>() {
                });

        // then
        assertThat(patientDtos).hasSize(2);
        assertThat(patientDtos).extracting("lastName").containsExactly("Brown", "Peterson");
        assertThat(patientDtos).extracting("middleName").containsExactly("Peter", "Mac");
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
        var createdPatientId = givenPatient("Adam", "Peter", "Brown",
                LocalDate.of(1985, 1, 1), "abrown@gmail.com", "+7(915)333-22-11", "p-1 info");

        // then
        var patientDto = RestAssured.given()
                .get("/api/v1/patients/{id}", createdPatientId.asString())
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PatientDto>() {
                });

        assertThat(patientDto.getId()).isEqualTo(createdPatientId.asString());
        assertThat(patientDto.getFirstName()).isEqualTo("Adam");
        assertThat(patientDto.getMiddleName()).isEqualTo("Peter");
        assertThat(patientDto.getLastName()).isEqualTo("Brown");
        assertThat(patientDto.getBirthDate()).isEqualTo(LocalDate.of(1985, 1, 1));
        assertThat(patientDto.getEmail()).isEqualTo("abrown@gmail.com");
        assertThat(patientDto.getPhone()).isEqualTo("+7(915)333-22-11");
        assertThat(patientDto.getInfo()).isEqualTo("p-1 info");
        assertThat(patientDto.getCreated()).isNotNull();
        assertThat(patientDto.getUpdated()).isNotNull();
        assertThat(patientDto.getCreated()).isEqualTo(patientDto.getUpdated());
        assertThat(patientDto.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("API updates existing patient")
    void update() {
        // given
        var patientId = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "+7(915)333-22-11", "p-1 info");

        // when
        RestAssured.given()
                .contentType("application/json")
                .body(PatientDto.builder()
                        .firstName("Adam-upd")
                        .middleName("Peter-upd")
                        .lastName("Brown-upd")
                        .birthDate(LocalDate.of(1984, 12, 11))
                        .email("abrown-upd@gmail.com")
                        .phone("111-222")
                        .info("p-1 info-upd")
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

        assertThat(patientDto.getId()).isEqualTo(patientId.asString());
        assertThat(patientDto.getFirstName()).isEqualTo("Adam-upd");
        assertThat(patientDto.getMiddleName()).isEqualTo("Peter-upd");
        assertThat(patientDto.getLastName()).isEqualTo("Brown-upd");
        assertThat(patientDto.getBirthDate()).isEqualTo(LocalDate.of(1984, 12, 11));
        assertThat(patientDto.getEmail()).isEqualTo("abrown-upd@gmail.com");
        assertThat(patientDto.getPhone()).isEqualTo("111-222");
        assertThat(patientDto.getInfo()).isEqualTo("p-1 info-upd");
        assertThat(patientDto.getCreated()).isNotNull();
        assertThat(patientDto.getUpdated()).isNotNull();
        assertThat(patientDto.getCreated()).isNotEqualTo(patientDto.getUpdated());
        assertThat(patientDto.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("API soft-deletes existing patient")
    void softDelete() {
        // given
        var patientId = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "+7(915)333-22-11", "p-1 info");

        // when
        RestAssured.given()
                .contentType("application/json")
                .delete("/api/v1/patients/{id}", patientId.asString())
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

        assertThat(patientDto.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @DisplayName("Creating patient with invalid phone number should return error")
    void createWithInvalidPhone() {
        // when
        var error = createPatientWithError("Adam", "Peter", "Brown",
                LocalDate.of(1985, 1, 1), "abrown@gmail.com", "+7(915)333-22", "p-1 info");

        // then
        assertThat(error.getStatusCode()).isEqualTo(400);
        assertThat(error.getMessage()).isEqualTo("Bad request, please check your data");
    }

    static List<PatientId> givenPatients() {
        var patientId_1 = givenPatient("Adam", "Peter", "Brown", LocalDate.of(1985, 1, 1), "abrown@gmail.com", "+7(915)333-22-11", "p-1 info");
        var patientId_2 = givenPatient("John", "Mac", "Peterson", LocalDate.of(1985, 3, 3), "jpeterson@gmail.com", "+7(915)333-22-33", "p-3 info");
        var patientId_3 = givenPatient("Mike", "Robert", "Charles", LocalDate.of(1985, 2, 2), "mcharles@gmail.com", "+7(915)333-22-22", "p-2 info");

        return List.of(patientId_1, patientId_2, patientId_3);
    }

    static PatientId givenPatient(String firstName, String middleName, String lastName, LocalDate birthDate, String email, String phone, String info) {
        String createdPatientId = RestAssured.given()
                .contentType("application/json")
                .body(PatientCreateOrUpdateRequest.builder()
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

        return PatientId.of(createdPatientId);
    }

    static ErrorRepresentation createPatientWithError(String firstName, String middleName, String lastName, LocalDate birthDate, String email, String phone, String info) {
        return RestAssured.given()
                .contentType("application/json")
                .body(PatientCreateOrUpdateRequest.builder()
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
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorRepresentation.class);
    }

}
