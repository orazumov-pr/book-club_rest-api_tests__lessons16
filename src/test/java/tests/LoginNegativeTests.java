package tests;

import models.LoginBodyModel;
import models.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static specs.LoginSpec.unauthorizedResponseSpec;
import static tests.TestData.*;


public class LoginNegativeTests extends TestBase {

    private static final String LOGIN_ENDPOINT = "/auth/token/";

    @Test
    @DisplayName("Логин с неверным паролем должен вернуть 401")
    void wrongPasswordReturn401() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel response = given()
                .spec(requestSpecification)
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
    void nonExistentUsernameReturn401() {
        LoginBodyModel loginData = new LoginBodyModel(NON_EXISTENT_USERNAME, LOGIN_PASSWORD);

        WrongCredentialsLoginResponseModel response = given()
                .spec(requestSpecification)
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
    void loginWithoutContentTypeReturn415() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

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
