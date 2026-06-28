package tests;

import models.login.BlankCredentialsLoginResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = step(
                "Авторизация с валидными данными и проверка ответа (200)", () ->
                        given(baseRequestSpec)
                                .body(loginData)
                                .when()
                                .post("/auth/token/")
                                .then()
                                .spec(successfulLoginResponseSpec)
                                .extract().as(SuccessfulLoginResponseModel.class)
        );

        step("Проверка полученных токенов", () -> {
            assertThat(loginResponse.access()).startsWith(TestData.EXPECTED_JWT_PREFIX);
            assertThat(loginResponse.refresh()).startsWith(TestData.EXPECTED_JWT_PREFIX);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(TestData.VALID_USERNAME, TestData.WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = step(
                "Авторизация с неверным паролем и проверка ответа (401)", () ->
                        given(baseRequestSpec)
                                .body(loginData)
                                .when()
                                .post("/auth/token/")
                                .then()
                                .spec(wrongCredentialsLoginResponseSpec)
                                .extract().as(WrongCredentialsLoginResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(loginResponse.detail()).isEqualTo(TestData.INVALID_CREDENTIALS_ERROR)
        );
    }

    @Test
    public void wrongUsernameLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(TestData.WRONG_USERNAME, TestData.VALID_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = step(
                "Авторизация с неверным логином и проверка ответа (401)", () ->
                        given(baseRequestSpec)
                                .body(loginData)
                                .when()
                                .post("/auth/token/")
                                .then()
                                .spec(wrongCredentialsLoginResponseSpec)
                                .extract().as(WrongCredentialsLoginResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(loginResponse.detail()).isEqualTo(TestData.INVALID_CREDENTIALS_ERROR)
        );
    }

    @Test
    public void emptyCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(TestData.EMPTY_VALUE, TestData.EMPTY_VALUE);

        BlankCredentialsLoginResponseModel loginResponse = step(
                "Авторизация с пустыми полями и проверка ответа (400)", () ->
                        given(baseRequestSpec)
                                .body(loginData)
                                .when()
                                .post("/auth/token/")
                                .then()
                                .spec(blankCredentialsLoginResponseSpec)
                                .extract().as(BlankCredentialsLoginResponseModel.class)
        );

        step("Проверка текста ошибок валидации", () -> {
            assertThat(loginResponse.username().get(0)).isEqualTo(TestData.BLANK_FIELD_ERROR);
            assertThat(loginResponse.password().get(0)).isEqualTo(TestData.BLANK_FIELD_ERROR);
        });
    }
}
