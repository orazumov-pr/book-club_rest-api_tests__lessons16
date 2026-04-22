package tests;

import com.github.javafaker.Faker;
import models.RegistrationBodyRecordsModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestDataGenerator;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static specs.RegistrationSpecs.*;

public class RegistrationNegativeTests {

    private static final String REGISTER_ENDPOINT = "/auth/register/";

    @Test
    @DisplayName("Регистрация с пустым username должна вернуть 400")
    void emptyUsernameReturn400() {
        RegistrationBodyRecordsModel requestBody = new RegistrationBodyRecordsModel(
                "",
                TestDataGenerator.generateValidPassword());


        given()
                .spec(registrationRequestSpec)
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
        RegistrationBodyRecordsModel requestBody = new RegistrationBodyRecordsModel(
                TestDataGenerator.generateValidUsername(),"");

        given()
                .spec(registrationRequestSpec)
                .body(requestBody)
                .when()
                .post(REGISTER_ENDPOINT)
                .then()
                .spec(error400ResponseSpec)
                .body("$", hasKey("password"));
    }

}