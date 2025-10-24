package br.com.kenjix.controllers;

import br.com.kenjix.config.TestConfigs;
import br.com.kenjix.data.dto.AgencyLocationDTO;
import br.com.kenjix.data.dto.security.AccountCredentialsDTO;
import br.com.kenjix.data.dto.security.TokenDTO;
import br.com.kenjix.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AgencyControllerTest  extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static TokenDTO tokenDto;
    private static AgencyLocationDTO agencyLocationDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        tokenDto = new TokenDTO();
        agencyLocationDTO = new AgencyLocationDTO();
    }
    @Test
    @Order(0)
    void signin() {
        AccountCredentialsDTO credentials =  new AccountCredentialsDTO("leandro", "admin123");

        tokenDto = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);


        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBasePath("/desafio/cadastrar")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(1)
    void register() throws JsonProcessingException {
        mockAgencyLocationDTO();
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(agencyLocationDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();


        Assert.assertEquals(content, "Agency Location regitered successfully!");
    }

    @Test
    @Order(2)
    void distance() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBasePath("/desafio/distancia")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("posX", 10)
                .queryParam("posY", 15.5)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        Map<String, String> result = objectMapper.readValue(content, Map.class);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("distância = 0", result.get("AGENCIA_1"));
    }

    private void mockAgencyLocationDTO() {
        agencyLocationDTO.setPosX(10.0);
        agencyLocationDTO.setPosY(15.5);
    }
}