package tests;

import api.ApiClient;
import com.github.javafaker.Faker;
import models.LoginBodyModel;
import models.RegistrationBodyRecordsModel;
import models.ClubsErrorResponseModel;
import models.CreateClubRequestModel;
import models.CreateClubResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ClubsDeleteTests extends TestBase{

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

        // Регистрация
        RegistrationBodyRecordsModel registrationData = new RegistrationBodyRecordsModel(username, password);
        api.users.register(registrationData);

        // Логин и получение токена
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
    @DisplayName("Успешное удаление существующего клуба должно вернуть 204")
    void deleteExistingClub_shouldReturn204() {
        // Создаем клуб
        CreateClubRequestModel createRequest = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );
        CreateClubResponseModel createdClub = api.club.createClub(accessToken, createRequest);
        int clubId = createdClub.id();

        // Удаляем клуб
        assertThatCode(() -> api.club.deleteClub(accessToken, clubId))
                .doesNotThrowAnyException();

        // Проверяем, что клуб действительно удален (404 при попытке получить)
        ClubsErrorResponseModel errorResponse = api.club.getClubByIdWithError(clubId, 404);
        assertThat(errorResponse.detail()).contains("Not found");
    }

    @Test
    @DisplayName("Удаление клуба владельцем должно быть успешным")
    void deleteClubByOwner_shouldReturn204() {
        // Создаем клуб
        CreateClubRequestModel createRequest = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );
        CreateClubResponseModel createdClub = api.club.createClub(accessToken, createRequest);
        int clubId = createdClub.id();

        // Проверяем, что владелец клуба - наш пользователь
        assertThat(createdClub.owner()).isNotNull();

        // Удаляем клуб
        assertThatCode(() -> api.club.deleteClub(accessToken, clubId))
                .doesNotThrowAnyException();
    }

    // ========== НЕГАТИВНЫЕ ТЕСТЫ ==========

    @Test
    @DisplayName("Удаление несуществующего клуба должно вернуть 404")
    void deleteNonExistentClub_shouldReturn404() {
        int nonExistentId = 999999;

        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, nonExistentId, 404);

        assertThat(response.detail()).contains("Not found");
    }

    @Test
    @DisplayName("Удаление клуба без токена авторизации должно вернуть 401")
    void deleteClubWithoutToken_shouldReturn401() {
        // Создаем клуб
        CreateClubRequestModel createRequest = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );
        CreateClubResponseModel createdClub = api.club.createClub(accessToken, createRequest);
        int clubId = createdClub.id();

        // Пытаемся удалить клуб без токена
        ClubsErrorResponseModel response = api.club.deleteClubWithError(null, clubId, 401);

        assertThat(response.detail()).isEqualTo("Authentication credentials were not provided.");
    }

    @Test
    @DisplayName("Удаление клуба с невалидным токеном должно вернуть 401")
    void deleteClubWithInvalidToken_shouldReturn401() {
        // Создаем клуб
        CreateClubRequestModel createRequest = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );
        CreateClubResponseModel createdClub = api.club.createClub(accessToken, createRequest);
        int clubId = createdClub.id();

        // Пытаемся удалить клуб с невалидным токеном
        String invalidToken = "invalid.token.here";
        ClubsErrorResponseModel response = api.club.deleteClubWithError(invalidToken, clubId, 401);

        assertThat(response.detail()).contains("invalid", "expired");
    }

    @Test
    @DisplayName("Удаление клуба с отрицательным ID должно вернуть 404")
    void deleteClubWithNegativeId_shouldReturn404() {
        int negativeId = -1;

        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, negativeId, 404);

        assertThat(response.detail()).contains("Not found");
    }

    @Test
    @DisplayName("Удаление клуба с ID = 0 должно вернуть 404")
    void deleteClubWithZeroId_shouldReturn404() {
        int zeroId = 0;

        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, zeroId, 404);

        assertThat(response.detail()).contains("Not found");
    }

    @Test
    @DisplayName("Повторное удаление уже удаленного клуба должно вернуть 404")
    void deleteAlreadyDeletedClub_shouldReturn404() {
        // Создаем клуб
        CreateClubRequestModel createRequest = new CreateClubRequestModel(
                bookTitle, bookAuthors, publicationYear, description, telegramChatLink
        );
        CreateClubResponseModel createdClub = api.club.createClub(accessToken, createRequest);
        int clubId = createdClub.id();

        // Удаляем клуб первый раз
        api.club.deleteClub(accessToken, clubId);

        // Пытаемся удалить клуб второй раз
        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, clubId, 404);

        assertThat(response.detail()).contains("Not found");
    }
}