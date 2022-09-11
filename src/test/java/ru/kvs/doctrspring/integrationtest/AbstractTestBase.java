package ru.kvs.doctrspring.integrationtest;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.kvs.doctrspring.config.DatabaseTestConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

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
@AutoConfigureMockMvc
abstract public class AbstractTestBase {

    protected static final long WRONG_ID = 404L;

    @Autowired
    protected MockMvc mockMvc;

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

    protected String testResource(String filePath) {
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filePath)))).lines().collect(Collectors.joining("\n"));
    }

}
