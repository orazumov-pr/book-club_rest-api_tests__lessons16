package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.*;
import specs.ClubsSpec;
import tests.TestBase;
import static io.restassured.RestAssured.given;
import static specs.ClubsSpec.*;
import static specs.RegistrationSpecs.requestSpecification;
import static specs.UserSpec.successfulRegistrationResponseSpec;


public class UserApi {

    private static final String REGISTER_ENDPOINT = "/users/register/";

    public static RegistrationResponseRecordsModel registerUser(String username, String password) {
        RegistrationBodyRecordsModel body = new RegistrationBodyRecordsModel(username, password);

        return given()
                .spec(requestSpecification)
                .body(body)
                .when()
                .post(REGISTER_ENDPOINT)
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
                .post(REGISTER_ENDPOINT)
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

    public RegistrationResponseRecordsModel register(RegistrationBodyRecordsModel registrationData) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .baseUri("https://book-club.qa.guru")  // Добавьте явно
                .basePath("/api/v1")
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseRecordsModel.class);
    }

}

