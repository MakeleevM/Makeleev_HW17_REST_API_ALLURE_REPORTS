package specs;

import io.restassured.specification.RequestSpecification;

import static allure.CustomsAllureListener.withCustomTemplate;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    public static RequestSpecification baseRequestSpec = with()
            .filter(withCustomTemplate())
            .log().all()
            .contentType(JSON)
            .basePath("/api/v1");
}
