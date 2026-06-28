package specs.updateuser;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import specs.BaseSpec;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class UpdateUserSpec extends BaseSpec {

    public static RequestSpecification authenticatedUpdateUserRequestSpec(String accessToken) {
        return with()
                .spec(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken);
    }

    public static ResponseSpecification successfulUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/updateuser/successful_update_user_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("remoteAddr", notNullValue())
            .build();

    public static ResponseSpecification putUpdateUserMissingFieldsResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/updateuser/put_missing_fields_update_user_response_schema.json"))
            .expectBody("username", notNullValue())
            .expectBody("lastName", notNullValue())
            .expectBody("email", notNullValue())
            .build();

    public static ResponseSpecification putUpdateUserMissingUsernameResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/updateuser/put_missing_username_update_user_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification unauthorizedUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/updateuser/unauthorized_update_user_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();
}
