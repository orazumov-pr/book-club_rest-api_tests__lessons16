package tests;

import models.LoginBodyModel;
import models.LogoutBodyModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

public class LogoutTests extends TestBase {

//    @Test
//    public void successfulLogoutTest() {
//        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
//        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);
//
//        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
//        api.auth.logout(logoutData);
//    }

    @Test
    public void successfulLogoutTestWithStep() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = step("Получение данных логин-пароль", () -> {
            return api.auth.loginAndGetRefreshToken(loginData);
        });

        step("Получение refresh", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
            api.auth.logout(logoutData);
        });
    }

}
