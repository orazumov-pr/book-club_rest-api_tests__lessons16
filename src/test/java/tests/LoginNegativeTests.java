package tests;

import models.LoginBodyModel;
import models.ValidationErrorLoginResponseModel;
import models.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.LoginTestDataGenerator;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.LoginSpec.*;


public class LoginNegativeTests extends TestBase {

    private static final String LOGIN_ENDPOINT = "/auth/token/";

    @Test
    @DisplayName("Логин с неверным паролем должен вернуть 401")
    void loginWithWrongPassword_shouldReturn401() {
        LoginBodyModel loginData = new LoginBodyModel(
                LoginTestDataGenerator.VALID_USERNAME,
                LoginTestDataGenerator.WRONG_PASSWORD
        );

        WrongCredentialsLoginResponseModel response = given()
                .spec(loginRequestSpec)
                .body(loginData)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(unauthorizedResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/login/wrong_credentials_login_response_schema.json"))
                .body("detail", notNullValue())
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);

        assertThat(response.detail()).isEqualTo("Invalid username or password.");
    }

    @Test
    @DisplayName("Логин с несуществующим username должен вернуть 401")
    void loginWithNonExistentUsername_shouldReturn401() {
        LoginBodyModel loginData = new LoginBodyModel(
                LoginTestDataGenerator.NON_EXISTENT_USERNAME,
                LoginTestDataGenerator.VALID_PASSWORD
        );

        WrongCredentialsLoginResponseModel response = given()
                .spec(loginRequestSpec)
                .body(loginData)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(unauthorizedResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);

        assertThat(response.detail()).isEqualTo("Invalid username or password.");
    }

    @Test
    @DisplayName("Логин без Content-Type заголовка")
    void loginWithoutContentType_shouldReturn415() {
        LoginBodyModel loginData = new LoginBodyModel(
                LoginTestDataGenerator.VALID_USERNAME,
                LoginTestDataGenerator.VALID_PASSWORD
        );

        given()
                .log().all()
                .body(loginData)
                .basePath("/api/v1")
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().all()
                .statusCode(415);
    }

}
