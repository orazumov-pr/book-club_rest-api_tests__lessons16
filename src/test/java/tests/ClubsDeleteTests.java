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
    @DisplayName("Удаление клуба владельцем должно быть успешным")
    void deleteClubByOwnerReturn204() {
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
    void deleteNonExistentClubReturn404() {
        int nonExistentId = 999999;

        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, nonExistentId, 404);

        assertThat(response.detail()).contains("No Club matches the given query.");
    }

     @Test
    @DisplayName("Удаление клуба с отрицательным ID должно вернуть 404")
    void deleteClubWithNegativeIdReturn404() {
        int negativeId = -1;

        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, negativeId, 404);

        assertThat(response.detail()).contains("No Club matches the given query.");
    }

    @Test
    @DisplayName("Удаление клуба с ID = 0 должно вернуть 404")
    void deleteClubWithZeroIdReturn404() {
        int zeroId = 0;

        ClubsErrorResponseModel response = api.club.deleteClubWithError(accessToken, zeroId, 404);

        assertThat(response.detail()).contains("No Club matches the given query.");
    }

    @Test
    @DisplayName("Повторное удаление уже удаленного клуба должно вернуть 404")
    void deleteAlreadyDeletedClubReturn404() {
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

        assertThat(response.detail()).contains("No Club matches the given query.");
    }
}