package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.updateuser.PatchUpdateUserFirstNameBodyModel;
import models.updateuser.PutUpdateUserMissingFieldsResponseModel;
import models.updateuser.PutUpdateUserMissingUsernameResponseModel;
import models.updateuser.PutUpdateUserPartialBodyModel;
import models.updateuser.PutUpdateUserWithoutUsernameBodyModel;
import models.updateuser.SuccessfulUpdateUserResponseModel;
import models.updateuser.UnauthorizedUpdateUserResponseModel;
import models.updateuser.UpdateUserBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static specs.updateuser.UpdateUserSpec.*;

public class UpdateUserTests extends TestBase {

    String username;
    String password;
    String accessToken;

    @BeforeEach
    public void prepareAuthUser() {
        username = TestData.randomUsername();
        password = TestData.randomPassword();

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        step("Регистрация пользователя для теста", () ->
                given(baseRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationResponseSpec)
        );

        LoginBodyModel loginData = new LoginBodyModel(username, password);

        SuccessfulLoginResponseModel loginResponse = step("Авторизация и получение access-токена", () ->
                given(baseRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract()
                        .as(SuccessfulLoginResponseModel.class)
        );

        accessToken = loginResponse.access();
    }

    @Test
    public void successfulPutUpdateUserTest() {
        String firstName = TestData.randomFirstName();
        String lastName = TestData.randomLastName();
        String email = TestData.randomEmail();

        UpdateUserBodyModel updateData = new UpdateUserBodyModel(username, firstName, lastName, email);

        SuccessfulUpdateUserResponseModel updateResponse = step(
                "PUT-обновление профиля пользователя и проверка ответа (200)", () ->
                        given(authenticatedUpdateUserRequestSpec(accessToken))
                                .body(updateData)
                                .when()
                                .put("/users/me/")
                                .then()
                                .spec(successfulUpdateUserResponseSpec)
                                .extract()
                                .as(SuccessfulUpdateUserResponseModel.class)
        );

        step("Проверка обновлённых данных пользователя", () -> {
            assertThat(updateResponse.username()).isEqualTo(username);
            assertThat(updateResponse.firstName()).isEqualTo(firstName);
            assertThat(updateResponse.lastName()).isEqualTo(lastName);
            assertThat(updateResponse.email()).isEqualTo(email);
            assertThat(updateResponse.remoteAddr()).matches(TestData.IP_ADDR_REGEXP);
        });
    }

    @Test
    public void successfulPatchUpdateUserTest() {
        String firstName = TestData.randomFirstName();

        PatchUpdateUserFirstNameBodyModel updateData = new PatchUpdateUserFirstNameBodyModel(firstName);

        SuccessfulUpdateUserResponseModel updateResponse = step(
                "PATCH-обновление firstName и проверка ответа (200)", () ->
                        given(authenticatedUpdateUserRequestSpec(accessToken))
                                .body(updateData)
                                .when()
                                .patch("/users/me/")
                                .then()
                                .spec(successfulUpdateUserResponseSpec)
                                .extract()
                                .as(SuccessfulUpdateUserResponseModel.class)
        );

        step("Проверка частично обновлённых данных", () -> {
            assertThat(updateResponse.username()).isEqualTo(username);
            assertThat(updateResponse.firstName()).isEqualTo(firstName);
            assertThat(updateResponse.lastName()).isEqualTo(TestData.EMPTY_VALUE);
            assertThat(updateResponse.email()).isEqualTo(TestData.EMPTY_VALUE);
        });
    }

    @Test
    public void putUpdateUserMissingRequiredFieldsTest() {
        PutUpdateUserPartialBodyModel updateData = new PutUpdateUserPartialBodyModel(TestData.randomFirstName());

        PutUpdateUserMissingFieldsResponseModel updateResponse = step(
                "PUT-обновление без обязательных полей и проверка ответа (400)", () ->
                        given(authenticatedUpdateUserRequestSpec(accessToken))
                                .body(updateData)
                                .when()
                                .put("/users/me/")
                                .then()
                                .spec(putUpdateUserMissingFieldsResponseSpec)
                                .extract()
                                .as(PutUpdateUserMissingFieldsResponseModel.class)
        );

        step("Проверка ошибок валидации", () -> {
            assertThat(updateResponse.username().get(0)).isEqualTo(TestData.REQUIRED_FIELD_ERROR);
            assertThat(updateResponse.lastName().get(0)).isEqualTo(TestData.REQUIRED_FIELD_ERROR);
            assertThat(updateResponse.email().get(0)).isEqualTo(TestData.REQUIRED_FIELD_ERROR);
        });
    }

    @Test
    public void putUpdateUserMissingUsernameTest() {
        PutUpdateUserWithoutUsernameBodyModel updateData = new PutUpdateUserWithoutUsernameBodyModel(
                TestData.randomFirstName(),
                TestData.randomLastName(),
                TestData.randomEmail()
        );

        PutUpdateUserMissingUsernameResponseModel updateResponse = step(
                "PUT-обновление без username и проверка ответа (400)", () ->
                        given(authenticatedUpdateUserRequestSpec(accessToken))
                                .body(updateData)
                                .when()
                                .put("/users/me/")
                                .then()
                                .spec(putUpdateUserMissingUsernameResponseSpec)
                                .extract()
                                .as(PutUpdateUserMissingUsernameResponseModel.class)
        );

        step("Проверка ошибки валидации username", () ->
                assertThat(updateResponse.username().get(0)).isEqualTo(TestData.REQUIRED_FIELD_ERROR)
        );
    }

    @Test
    public void unauthorizedPutUpdateUserTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                username,
                TestData.randomFirstName(),
                TestData.randomLastName(),
                TestData.randomEmail()
        );

        UnauthorizedUpdateUserResponseModel updateResponse = step(
                "PUT-обновление без авторизации и проверка ответа (401)", () ->
                        given(baseRequestSpec)
                                .body(updateData)
                                .when()
                                .put("/users/me/")
                                .then()
                                .spec(unauthorizedUpdateUserResponseSpec)
                                .extract()
                                .as(UnauthorizedUpdateUserResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(updateResponse.detail()).isEqualTo(TestData.AUTH_CREDENTIALS_NOT_PROVIDED_ERROR)
        );
    }

    @Test
    public void unauthorizedPatchUpdateUserTest() {
        PatchUpdateUserFirstNameBodyModel updateData = new PatchUpdateUserFirstNameBodyModel(TestData.randomFirstName());

        UnauthorizedUpdateUserResponseModel updateResponse = step(
                "PATCH-обновление без авторизации и проверка ответа (401)", () ->
                        given(baseRequestSpec)
                                .body(updateData)
                                .when()
                                .patch("/users/me/")
                                .then()
                                .spec(unauthorizedUpdateUserResponseSpec)
                                .extract()
                                .as(UnauthorizedUpdateUserResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(updateResponse.detail()).isEqualTo(TestData.AUTH_CREDENTIALS_NOT_PROVIDED_ERROR)
        );
    }
}
