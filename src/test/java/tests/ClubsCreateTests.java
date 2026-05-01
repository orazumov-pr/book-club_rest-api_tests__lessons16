package tests;

import api.ApiClient;
import com.github.javafaker.Faker;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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
        bookTitle = faker.book().title();
        bookAuthors = faker.book().author();
        publicationYear = faker.number().numberBetween(1800, 2024);
        description = faker.lorem().paragraph();
        telegramChatLink = "https://t.me/" + faker.lorem().word();
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

    @Test
    @DisplayName("Создание клуба с минимальными данными должно вернуть 201")
    void createClubWithMinimalDataReturn201() {
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                "",  // Пустое описание
                ""   // Пустая ссылка на телеграм
        );

        CreateClubResponseModel response = api.club.createClub(accessToken, request);

        assertThat(response.id()).isNotNull();
        assertThat(response.bookTitle()).isEqualTo(bookTitle);
        assertThat(response.description()).isEmpty();
        assertThat(response.telegramChatLink()).isEmpty();
    }

    @Test
    @DisplayName("Создание клуба с максимальными значениями полей")
    void createClubWithMaxValues_shouldReturn201() {
        String longTitle = faker.lorem().characters(200);
        String longAuthors = faker.lorem().characters(500);
        Integer maxYear = 2147483647;  // Максимальное значение Integer

        CreateClubRequestModel request = new CreateClubRequestModel(
                longTitle, longAuthors, maxYear, description, telegramChatLink
        );

        CreateClubResponseModel response = api.club.createClub(accessToken, request);

        assertThat(response.id()).isNotNull();
        assertThat(response.bookTitle()).isEqualTo(longTitle);
        assertThat(response.bookAuthors()).isEqualTo(longAuthors);
        assertThat(response.publicationYear()).isEqualTo(maxYear);
    }

    // ========== НЕГАТИВНЫЕ ТЕСТЫ ==========

    @Test
    @DisplayName("Создание клуба без токена авторизации должно вернуть 401")
    void createClubWithoutToken_shouldReturn401() {
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(null, request, 401);

        assertThat(response.detail()).isEqualTo("Authentication credentials were not provided.");
    }

    @Test
    @DisplayName("Создание клуба с невалидным токеном должно вернуть 401")
    void createClubWithInvalidToken_shouldReturn401() {
        String invalidToken = "invalid.token.here";
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(invalidToken, request, 401);

        assertThat(response.detail()).isEqualTo("Token is invalid or expired");
    }

    @Test
    @DisplayName("Создание клуба с отрицательным publicationYear должно вернуть 400")
    void createClubWithNegativePublicationYear_shouldReturn400() {
        Integer negativeYear = -2024;
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, negativeYear, description, telegramChatLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(accessToken, request, 400);

        assertThat(response.detail()).isNotNull();
    }

    @Test
    @DisplayName("Создание клуба с publicationYear = 0 должно вернуть 400")
    void createClubWithZeroPublicationYear_shouldReturn400() {
        Integer zeroYear = 0;
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, zeroYear, description, telegramChatLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(accessToken, request, 400);

        assertThat(response.detail()).isNotNull();
    }

    @Test
    @DisplayName("Создание клуба с некорректной ссылкой на Telegram")
    void createClubWithInvalidTelegramLink_shouldReturn400() {
        String invalidTelegramLink = "invalid-url";
        CreateClubRequestModel request = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, invalidTelegramLink
        );

        ClubsErrorResponseModel response = api.club.createClubWithError(accessToken, request, 400);

        assertThat(response.detail()).isNotNull();
    }
}
