package tests;

import models.registration.BlankPasswordRegistrationResponseModel;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = TestData.randomUsername();
        password = TestData.randomPassword();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = step(
                "Регистрация нового пользователя и проверка ответа (201)", () ->
                        given(baseRequestSpec)
                                .body(registrationData)
                                .when()
                                .post("/users/register/")
                                .then()
                                .spec(successfulRegistrationResponseSpec)
                                .extract()
                                .as(SuccessfulRegistrationResponseModel.class)
        );

        step("Проверка данных зарегистрированного пользователя", () -> {
            assertThat(registrationResponse.username()).isEqualTo(username);
            assertThat(registrationResponse.id()).isGreaterThan(0);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
            assertThat(registrationResponse.remoteAddr()).matches(TestData.IP_ADDR_REGEXP);
        });
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = step(
                "Первая регистрация пользователя", () ->
                        given(baseRequestSpec)
                                .body(registrationData)
                                .when()
                                .post("/users/register/")
                                .then()
                                .spec(successfulRegistrationResponseSpec)
                                .extract()
                                .as(SuccessfulRegistrationResponseModel.class)
        );

        step("Проверка успешной первой регистрации", () ->
                assertThat(firstRegistrationResponse.username()).isEqualTo(username)
        );

        ExistingUserResponseModel secondRegistrationResponse = step(
                "Повторная регистрация с тем же username и проверка ответа (400)", () ->
                        given(baseRequestSpec)
                                .body(registrationData)
                                .when()
                                .post("/users/register/")
                                .then()
                                .spec(existingUserRegistrationResponseSpec)
                                .extract()
                                .as(ExistingUserResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(secondRegistrationResponse.username().get(0)).isEqualTo(TestData.EXISTING_USER_ERROR)
        );
    }

    @Test
    public void blankUsernameRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(TestData.EMPTY_VALUE, password);

        ExistingUserResponseModel registrationResponse = step(
                "Регистрация с пустым username и проверка ответа (400)", () ->
                        given(baseRequestSpec)
                                .body(registrationData)
                                .when()
                                .post("/users/register/")
                                .then()
                                .spec(blankUsernameRegistrationResponseSpec)
                                .extract()
                                .as(ExistingUserResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(registrationResponse.username().get(0)).isEqualTo(TestData.BLANK_FIELD_ERROR)
        );
    }

    @Test
    public void blankPasswordRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, TestData.EMPTY_VALUE);

        BlankPasswordRegistrationResponseModel registrationResponse = step(
                "Регистрация с пустым password и проверка ответа (400)", () ->
                        given(baseRequestSpec)
                                .body(registrationData)
                                .when()
                                .post("/users/register/")
                                .then()
                                .spec(blankPasswordRegistrationResponseSpec)
                                .extract()
                                .as(BlankPasswordRegistrationResponseModel.class)
        );

        step("Проверка текста ошибки", () ->
                assertThat(registrationResponse.password().get(0)).isEqualTo(TestData.BLANK_FIELD_ERROR)
        );
    }
}
