package tests;

import models.RegistrationBodyRecordsModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static specs.RegistrationSpecs.error400ResponseSpec;
import static specs.RegistrationSpecs.requestSpecification;
import static tests.TestData.*;

public class RegistrationNegativeTests extends TestBase  {


    private static final String REGISTER_ENDPOINT = "/users/register/";

    @Test
    @DisplayName("Регистрация с пустым username должна вернуть 400")
    void emptyUsernameReturn400() {
        RegistrationBodyRecordsModel requestBody = new RegistrationBodyRecordsModel(EMPTY_USERNAME, LOGIN_PASSWORD);


        given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post(REGISTER_ENDPOINT)
                .then()
                .spec(error400ResponseSpec)
                .body("$", hasKey("username"));
    }


    @Test
    @DisplayName("Регистрация с пустым password должна вернуть 400")
    void emptyPasswordReturn400() {
        RegistrationBodyRecordsModel requestBody = new RegistrationBodyRecordsModel(LOGIN_USERNAME,"");

        given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post("/users/register/")
                .then()
                .spec(error400ResponseSpec)
                .body("$", hasKey("password"));
    }

}