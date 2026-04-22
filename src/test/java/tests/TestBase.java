package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    protected static final ApiClient api = new ApiClient();

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru/api/v1";
        }
}
