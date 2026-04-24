package api;

import io.restassured.response.Response;
import models.LoginBodyModel;
import models.RegistrationBodyRecordsModel;
import models.RegistrationResponseRecordsModel;
import models.TokenResponseModel;
import tests.TestBase;
import static io.restassured.RestAssured.given;


public class UserApi {

    public static RegistrationResponseRecordsModel registerUser(String username, String password) {
        RegistrationBodyRecordsModel body = new RegistrationBodyRecordsModel(username, password);

        return given()
                .spec(TestBase.requestSpecification)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseRecordsModel.class);
    }

    public static TokenResponseModel login(String username, String password) {
        LoginBodyModel body = new LoginBodyModel(username, password);

        return given()
                .spec(TestBase.requestSpecification)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .as(TokenResponseModel.class);
    }

    public static Response loginWithInvalidCredentials(String username, String password) {
        LoginBodyModel body = new LoginBodyModel(username, password);

        return given()
                .spec(TestBase.requestSpecification)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();
    }

    public static Response registerUserRaw(String username, String password) {
        RegistrationBodyRecordsModel body = new RegistrationBodyRecordsModel(username, password);

        return given()
                .spec(TestBase.requestSpecification)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();
    }
}

