package tests;

import net.datafaker.Faker;

public class TestData {

    public static final String VALID_USERNAME = "qaguru";
    public static final String VALID_PASSWORD = "qaguru123";
    public static final String WRONG_USERNAME = "wronguser";
    public static final String WRONG_PASSWORD = "qaguru1234";
    public static final String EMPTY_VALUE = "";
    public static final String INVALID_REFRESH_TOKEN = "invalid.refresh.token";
    public static final String EXPECTED_JWT_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String INVALID_CREDENTIALS_ERROR = "Invalid username or password.";
    public static final String EXISTING_USER_ERROR = "A user with that username already exists.";
    public static final String BLANK_FIELD_ERROR = "This field may not be blank.";
    public static final String INVALID_TOKEN_ERROR = "Token is invalid";
    public static final String REQUIRED_FIELD_ERROR = "This field is required.";
    public static final String AUTH_CREDENTIALS_NOT_PROVIDED_ERROR = "Authentication credentials were not provided.";
    public static final String IP_ADDR_REGEXP = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
            + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    private static final Faker FAKER = new Faker();

    public static String randomUsername() {
        return FAKER.internet().username() + FAKER.number().digits(4);
    }

    public static String randomPassword() {
        return FAKER.name().firstName();
    }

    public static String randomFirstName() {
        return FAKER.name().firstName();
    }

    public static String randomLastName() {
        return FAKER.name().lastName();
    }

    public static String randomEmail() {
        return FAKER.internet().emailAddress();
    }
}
