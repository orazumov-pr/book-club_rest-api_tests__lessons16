package api;

import models.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.successfulLoginResponseSpec;
import static specs.LogoutSpec.*;


public class AuthApiClient {

    private static final String LOGIN_ENDPOINT = "/auth/token/";
    private static final String LOGOUT_ENDPOINT = "/auth/logout/";

    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    public void logout(LogoutBodyModel logoutBody) {
        given(baseRequestSpec)
                .body(logoutBody)
                .when()
                .post(LOGOUT_ENDPOINT)
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    public LogoutErrorResponseModel logoutWithError(LogoutBodyModel logoutBody, int expectedStatusCode) {
        return given(baseRequestSpec)
                .body(logoutBody)
                .when()
                .post(LOGOUT_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .as(LogoutErrorResponseModel.class);
    }

    public LogoutValidationErrorResponseModel logoutWithValidationError(LogoutBodyModel logoutBody) {
        return given(baseRequestSpec)
                .body(logoutBody)
                .when()
                .post(LOGOUT_ENDPOINT)
                .then()
                .spec(badRequestResponseSpec)
                .extract()
                .as(LogoutValidationErrorResponseModel.class);
    }

}
