package tests;

import models.login.LoginBodyModel;
import models.logout.BlankRefreshTokenLogoutResponseModel;
import models.logout.InvalidRefreshTokenResponseModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);

        String refreshToken = step("Авторизация и получение refresh-токена", () ->
                given(baseRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract().path("refresh")
        );

        step("Отправка запроса logout с refresh-токеном и проверка ответа (200)", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);

            given(baseRequestSpec)
                    .body(logoutData)
                    .when()
                    .post("/auth/logout/")
                    .then()
                    .spec(successfulLogoutResponseSpec);
        });
    }

    @Test
    public void invalidRefreshTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(TestData.INVALID_REFRESH_TOKEN);

        InvalidRefreshTokenResponseModel logoutResponse = step(
                "Отправка запроса logout с невалидным refresh-токеном и проверка ответа (401)", () ->
                        given(baseRequestSpec)
                                .body(logoutData)
                                .when()
                                .post("/auth/logout/")
                                .then()
                                .spec(invalidRefreshTokenLogoutResponseSpec)
                                .extract()
                                .as(InvalidRefreshTokenResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(logoutResponse.detail()).isEqualTo(TestData.INVALID_TOKEN_ERROR)
        );
    }

    @Test
    public void emptyRefreshTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(TestData.EMPTY_VALUE);

        BlankRefreshTokenLogoutResponseModel logoutResponse = step(
                "Отправка запроса logout с пустым refresh-токеном и проверка ответа (400)", () ->
                        given(baseRequestSpec)
                                .body(logoutData)
                                .when()
                                .post("/auth/logout/")
                                .then()
                                .spec(blankRefreshTokenLogoutResponseSpec)
                                .extract()
                                .as(BlankRefreshTokenLogoutResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(logoutResponse.refresh().get(0)).isEqualTo(TestData.BLANK_FIELD_ERROR)
        );
    }
}
