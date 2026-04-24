package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    protected static final ApiClient api = new ApiClient();
    protected static final String BASE_PATH = "/api/v1";
    public static RequestSpecification requestSpecification;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .setBasePath(BASE_PATH)
                .setContentType(io.restassured.http.ContentType.JSON)
                .build();
    }
}