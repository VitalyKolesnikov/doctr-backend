package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.kvs.doctrspring.config.DatabaseTestConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {DatabaseTestConfig.class})
@ContextConfiguration(
        initializers = {
                DatabaseTestConfig.class
        },
        classes = {
                DatabaseTestConfig.class
        }
)
@ActiveProfiles("integration-test")
@Sql(value = {"/sql/clearDb.sql", "/sql/user.sql", "/sql/clinics.sql"})
abstract public class AbstractTestBase {

    @LocalServerPort
    protected Integer localServerPort;

    @BeforeEach
    void setUp() {
        initRestAssured();
    }

    protected void initRestAssured() {
        RestAssured.port = localServerPort;
        RestAssured.config = RestAssuredConfig.newConfig()
                .encoderConfig(
                        EncoderConfig.encoderConfig()
                                .appendDefaultContentCharsetToContentTypeIfUndefined(false)
                                .defaultContentCharset("UTF-8")
                );
    }

}
