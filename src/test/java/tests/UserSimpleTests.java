package tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserSimpleTests {
    private static final String BASE_URI = "https://book-club.qa.guru";
    private static final String BASE_PATH = "/api/v1";

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String accessToken;
    private Integer userId;

    @BeforeEach
    void userSetUp() {
        Faker faker = new Faker();

        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();

        // Регистрация пользователя для всех тестов
        registerUser();

        // Логин и получение токена
        loginAndGetToken();
    }

    private void registerUser() {
        String registrationBody = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                username, password
        );

        JsonPath response = given()
                .log().ifValidationFails()
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(ContentType.JSON)
                .body(registrationBody)
                .when()
                .post("/users/register/")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .jsonPath();

        userId = response.getInt("id");
        assertThat(userId).isNotNull();
        assertThat(response.getString("username")).isEqualTo(username);
    }

    private void loginAndGetToken() {
        String loginBody = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                username, password
        );

        JsonPath response = given()
                .log().ifValidationFails()
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .jsonPath();

        accessToken = response.getString("access");
        assertThat(accessToken).isNotNull();
        assertThat(response.getString("refresh")).isNotNull();
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Disabled
    void successfulRegistrationTest() {
        String newUsername = "new_user_" + System.currentTimeMillis();
        String newPassword = "new_pass_" + System.currentTimeMillis();

        String registrationBody = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                newUsername, newPassword
        );

        JsonPath response = given()
                .log().ifValidationFails()
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(ContentType.JSON)
                .body(registrationBody)
                .when()
                .post("/users/register/")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .jsonPath();

        assertThat(response.getString("username")).isEqualTo(newUsername);
        assertThat(response.getInt("id")).isNotNull();
    }

    @Test
    @DisplayName("Логин с неверным паролем должен вернуть 401")
    @Disabled
    void loginWithWrongPasswordTest() {
        String loginBody = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                username, "wrong_password"
        );

        given()
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .statusCode(401)
                .body("detail", org.hamcrest.Matchers.equalTo("Invalid username or password."));
    }

    @Test
    @DisplayName("Логин с несуществующим username должен вернуть 401")
    @Disabled
    void loginWithNonExistentUsernameTest() {
        String loginBody = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                "nonexistent_user_" + System.currentTimeMillis(), password
        );

        given()
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .statusCode(401)
                .body("detail", org.hamcrest.Matchers.equalTo("Invalid username or password."));
    }

    @Test
    @DisplayName("Логин с пустым username должен вернуть 400")
    @Disabled
    void loginWithEmptyUsernameTest() {
        String loginBody = String.format(
                "{\"username\":\"%s\", \"password\":\"%s\"}",
                "", password
        );

        given()
                .baseUri(BASE_URI)
                .basePath(BASE_PATH)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .statusCode(400);
    }
}
