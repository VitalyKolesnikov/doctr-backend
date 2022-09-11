package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvs.doctrspring.adapters.restapi.dto.response.BaseDto;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ClinicDto;
import ru.kvs.doctrspring.adapters.restapi.dto.response.ErrorRepresentation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.kvs.doctrspring.domain.Status.ACTIVE;

public class ClinicIntegrationTest extends AbstractTestBase {

    @Test
    @DisplayName("API returns all active clinics of current user")
    void getAll() {

        // when
        var clinicDtos = RestAssured.given()
                .get("/api/v1/clinics/")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<ClinicDto>>() {
                });

        // then
        assertThat(clinicDtos).hasSize(2);
        assertThat(clinicDtos).allMatch(BaseDto::isActive);
    }

    @Test
    @DisplayName("API returns clinic by id")
    void getById() {
        // given
        var clinicId = 1004L;

        // when
        var clinicDto = RestAssured.given()
                .get("/api/v1/clinics/{id}", clinicId)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ClinicDto>() {
                });

        // then
        assertThat(clinicDto.getId()).isEqualTo(clinicId);
        assertThat(clinicDto.getName()).isEqualTo("Clinic1");
        assertThat(clinicDto.getPhone()).isEqualTo("+7(499)111-1111");
        assertThat(clinicDto.getAddress()).isEqualTo("Moscow, Lenina 1");
        assertThat(clinicDto.getStatus()).isEqualTo(ACTIVE);
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
