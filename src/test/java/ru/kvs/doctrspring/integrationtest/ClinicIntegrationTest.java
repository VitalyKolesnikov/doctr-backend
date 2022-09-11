package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvs.doctrspring.domain.BaseEntity;
import ru.kvs.doctrspring.domain.Clinic;
import ru.kvs.doctrspring.adapters.restapi.dto.ErrorRepresentation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

public class ClinicIntegrationTest extends AbstractTestBase {

    @Test
    @DisplayName("API returns all active clinics of current user")
    void getAll() {

        // when
        var clinics = RestAssured.given()
                .get("/api/v1/clinics/")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<Clinic>>() {
                });

        // then
        assertThat(clinics).hasSize(2);
        assertThat(clinics).allMatch(BaseEntity::isActive);
    }

    @Test
    @DisplayName("API returns clinic by id")
    void getById() {
        // given
        var clinicId = 1004L;

        // when
        var clinic = RestAssured.given()
                .get("/api/v1/clinics/{id}", clinicId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<Clinic>() {
                });

        // then
        assertThat(clinic.getId()).isEqualTo(clinicId);
        assertThat(clinic.getName()).isEqualTo("Clinic1");
        assertThat(clinic.getPhone()).isEqualTo("+7(499)111-1111");
        assertThat(clinic.getAddress()).isEqualTo("Moscow, Lenina 1");
        assertThat(clinic.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    @DisplayName("API returns 404 when clinic not found by id")
    void getByIdNotFound() {
        // when
        var errorRepresentation = RestAssured.given()
                .get("/api/v1/clinics/{id}", WRONG_ID)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .body().as(ErrorRepresentation.class);

        // then
        assertThat(errorRepresentation.getMessage())
                .isEqualTo(String.format("Clinic with id [%s] not found", WRONG_ID));
    }

}
