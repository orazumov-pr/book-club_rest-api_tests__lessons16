package tests;

import api.ApiClient;
import com.github.javafaker.Faker;
import models.ClubsErrorResponseModel;
import models.CreateClubRequestModel;
import models.CreateClubResponseModel;
import models.LoginBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClubsCreateTests extends TestBase {

    private ApiClient api;
    private Faker faker;
    private String accessToken;
    private String bookTitle;
    private String bookAuthors;
    private Integer publicationYear;
    private String description;
    private String telegramChatLink;

    @BeforeEach
    void setUpClubs() {
        api = new ApiClient();
        faker = new Faker();

        // Создаем тестового пользователя и получаем токен
        String username = "club_user_" + System.currentTimeMillis();
        String password = "pass_" + System.currentTimeMillis();

        // Регистрация и логин
        api.users.register(new models.RegistrationBodyRecordsModel(username, password));
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        // Подготовка тестовых данных для клуба
        bookTitle = faker.book().title() + " " + faker.number().numberBetween(1000, 9999);
        bookAuthors = faker.book().author();
        publicationYear = 2147483647;
        description = faker.lorem().paragraph();
        telegramChatLink = "https://t.me/qa_guru";   //faker.lorem().word();
    }

    // ========== ПОЗИТИВНЫЕ ТЕСТЫ ==========

    @Test
    @DisplayName("Создание клуба с валидными данными должно вернуть 201")
    void createClubReturn201() {
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );

        CreateClubResponseModel response = api.club.createClub(accessToken, request);

        assertThat(response.id()).isNotNull();
        assertThat(response.bookTitle()).isEqualTo(bookTitle);
        assertThat(response.bookAuthors()).isEqualTo(bookAuthors);
        assertThat(response.publicationYear()).isEqualTo(publicationYear);
        assertThat(response.description()).isEqualTo(description);
        assertThat(response.telegramChatLink()).isEqualTo(telegramChatLink);
        assertThat(response.owner()).isNotNull();
        assertThat(response.members()).isNotNull();
        assertThat(response.created()).isNotNull();
    }


    // ========== НЕГАТИВНЫЕ ТЕСТЫ ==========

    @Test
    @DisplayName("Создание клуба без токена авторизации должно вернуть 401")
    void createClubWithoutTokenReturn401() {
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(null, request, 401);

        assertThat(response.detail()).isEqualTo("Given token not valid for any token type");
    }

    @Test
    @DisplayName("Создание клуба с невалидным токеном должно вернуть 401")
    void createClubWithInvalidTokenReturn401() {
        String invalidToken = "invalid.token.here";
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(invalidToken, request, 401);

        assertThat(response.detail()).isEqualTo("Given token not valid for any token type");
    }

    @Test
    @DisplayName("Создание клуба с некорректной ссылкой на Telegram")
    void createClubWithInvalidTelegramLinkReturn400() {
        String invalidTelegramLink = "invalid-url";
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, invalidTelegramLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(accessToken, request, 400);

        // Проверяем, что есть ошибка в поле telegramChatLink
        assertThat(response.telegramChatLink()).isNotNull();
    }
}
