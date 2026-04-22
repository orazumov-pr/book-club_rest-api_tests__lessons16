package tests;

import api.AuthApiClient;
import models.LoginBodyModel;
import models.LogoutBodyModel;
import models.LogoutErrorResponseModel;
import models.LogoutValidationErrorResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tests.TestData.*;

public class LogoutNegativeTests extends TestBase {


    private AuthApiClient api;
    private String validRefreshToken;

    @BeforeEach
    void LogoutSetUp() {
        api = new AuthApiClient();
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        validRefreshToken = api.loginAndGetRefreshToken(loginData);
    }

    // ========== ТЕСТЫ С НЕВАЛИДНЫМИ ТОКЕНАМИ ==========

    @Test
    @DisplayName("Логаут с невалидным refresh токеном должен вернуть 401")
    void logoutWithInvalidRefreshToken_shouldReturn401() {
        LogoutBodyModel logoutData = new LogoutBodyModel(INVALID_REFRESH_TOKEN);

        LogoutErrorResponseModel response = api.logoutWithError(logoutData, 401);

        assertThat(response.detail()).isEqualTo("Token is invalid or expired");
    }

    @Test
    @DisplayName("Логаут с пустым refresh токеном должен вернуть 400")
    void logoutWithEmptyRefreshToken_shouldReturn400() {
        LogoutBodyModel logoutData = new LogoutBodyModel("");

        LogoutValidationErrorResponseModel response = api.logoutWithValidationError(logoutData);

        assertThat(response.refresh()).contains("This field may not be blank.");
    }
}
