package tests;

import api.ApiClient;
import io.restassured.response.Response;
import models.RegistrationResponseRecordsModel;
import models.TokenResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.UserApi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserTests extends TestBase {

    private String username;
    private String password;
    private String accessToken;
    private Integer userId;

    private ApiClient apiClient;

    @BeforeEach
    void userSetUp() {
        apiClient = new ApiClient();

        // Используем уникальные данные для каждого теста
        username = TestData.generateUniqueUsername();
        password = TestData.generateUniquePassword();

        // Регистрация пользователя для всех тестов
        RegistrationResponseRecordsModel registrationResponse = apiClient.users.registerUser(username, password);
        userId = registrationResponse.id();
        assertThat(userId).isNotNull();
        assertThat(registrationResponse.username()).isEqualTo(username);

        // Логин и получение токена
        TokenResponseModel tokenResponse = apiClient.users.login(username, password);
        accessToken = tokenResponse.access();
        assertThat(accessToken).isNotNull();
        assertThat(tokenResponse.refresh()).isNotNull();
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void successfulRegistrationTest() {
        String newUsername = TestData.generateUniqueUsername();
        String newPassword = TestData.generateUniquePassword();

        RegistrationResponseRecordsModel response = apiClient.users.registerUser(newUsername, newPassword);

        assertThat(response.username()).isEqualTo(newUsername);
        assertThat(response.id()).isNotNull();
    }

    @Test
    @DisplayName("Логин с неверным паролем должен вернуть 401")
    void loginWithWrongPasswordTest() {
        Response response = apiClient.users.loginWithInvalidCredentials(username, TestData.WRONG_PASSWORD);

        response.then()
                .statusCode(401)
                .body("detail", equalTo(TestData.INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    @DisplayName("Логин с несуществующим username должен вернуть 401")
    void loginWithNonExistentUsernameTest() {
        Response response = apiClient.users.loginWithInvalidCredentials(
                TestData.NON_EXISTENT_USERNAME,
                password
        );

        response.then()
                .statusCode(401)
                .body("detail", equalTo(TestData.INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    @DisplayName("Логин с пустым username должен вернуть 400")
    void loginWithEmptyUsernameTest() {
        Response response = apiClient.users.loginWithInvalidCredentials(
                TestData.EMPTY_USERNAME,
                password
        );

        response.then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Регистрация с уже существующим username должна вернуть ошибку")
    void registrationWithExistingUsernameTest() {
        // Пытаемся зарегистрировать пользователя с уже существующим username
        Response response = apiClient.users.registerUserRaw(username, TestData.generateUniquePassword());

        response.then()
                .statusCode(400);
    }
}