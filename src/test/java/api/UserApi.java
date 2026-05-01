package api;

import io.restassured.response.Response;
import models.*;
import specs.ClubsSpec;
import tests.TestBase;
import static io.restassured.RestAssured.given;
import static specs.ClubsSpec.successfulClubCreateResponseSpec;
import static specs.RegistrationSpecs.requestSpecification;


public class UserApi {

    public static RegistrationResponseRecordsModel registerUser(String username, String password) {
        RegistrationBodyRecordsModel body = new RegistrationBodyRecordsModel(username, password);

        return given()
                .spec(requestSpecification)
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
                .spec(requestSpecification)
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
                .spec(requestSpecification)
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
                .spec(requestSpecification)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .log().ifValidationFails()
                .extract()
                .response();
    }

    public CreateClubResponseModel createClub(String accessToken, CreateClubRequestModel createClubBody) {
        return given(requestSpecification)
                .header("Authorization", "Bearer " + accessToken)
                .body(createClubBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulClubCreateResponseSpec)
                .extract()
                .as(CreateClubResponseModel.class);
    }

}

