package api;

import models.*;

import static io.restassured.RestAssured.given;
import static specs.LoginSpec.successfulLoginResponseSpec;
import static specs.LogoutSpec.*;
import static tests.TestBase.requestSpecification;


public class AuthApiClient {

    private static final String LOGIN_ENDPOINT = "/auth/token/";
    private static final String LOGOUT_ENDPOINT = "/auth/logout/";

    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(requestSpecification)
                .body(loginBody)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given(requestSpecification)
                .body(loginBody)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    public void logout(LogoutBodyModel logoutBody) {
        given(requestSpecification)
                .body(logoutBody)
                .when()
                .post(LOGOUT_ENDPOINT)
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    public LogoutErrorResponseModel logoutWithError(LogoutBodyModel logoutBody, int expectedStatusCode) {
        return given(requestSpecification)
                .body(logoutBody)
                .when()
                .post(LOGOUT_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .as(LogoutErrorResponseModel.class);
    }

    public LogoutValidationErrorResponseModel logoutWithValidationError(LogoutBodyModel logoutBody) {
        return given(requestSpecification)
                .body(logoutBody)
                .when()
                .post(LOGOUT_ENDPOINT)
                .then()
                .spec(badRequestResponseSpec)
                .extract()
                .as(LogoutValidationErrorResponseModel.class);
    }

}
