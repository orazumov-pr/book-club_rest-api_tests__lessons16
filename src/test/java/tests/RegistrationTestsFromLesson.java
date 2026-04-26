package tests;

import com.github.javafaker.Faker;
import models.RegistrationBodyRecordsModel;
import models.RegistrationResponseRecordsModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTestsFromLesson {

    private static final String BASE_URL = "https://book-club.qa.guru/api/v1/users/register/";

    String username;
    String password;

    @Test
    public void successfulRegistrationTest_bad_practice() {

        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.name().firstName();

        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        given()
                .log().all()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(username))
                .body("id", notNullValue());
    }

    @Test
    @Disabled
    public void existingUser400Test() {

        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.name().firstName();

        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        given()
                .log().all()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(201)
                .body("username[0]", is("A user with that username already exists."));
    }

    @Test
    @Disabled
    public void successfulRegistrationTest_with_records(){
        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(username, password);

        RegistrationResponseRecordsModel registrationResponse = given()
                .log().all()
                .contentType(JSON)
                .body(data)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(RegistrationResponseRecordsModel.class);

        assertEquals(username, registrationResponse.username());
    }

}
