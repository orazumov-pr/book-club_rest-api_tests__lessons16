package specs;


import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.*;

public class ClubsSpec {

    private static final String CLUBS_ENDPOINT = "/clubs/";
    private static final String CLUB_BY_ID_ENDPOINT = "/clubs/{id}/";

    public static RequestSpecification clubsRequestSpec = with()
            .log().all()
            .contentType(ContentType.JSON)
            .basePath("/api/v1");

    // Успешный ответ с клубами
    public static ResponseSpecification successfulClubsListResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("count", notNullValue())
            .expectBody("results", notNullValue())
            .build();

    // Успешное создание клуба
    public static ResponseSpecification successfulClubCreateResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody("id", notNullValue())
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .expectBody("publicationYear", notNullValue())
            .expectBody("owner", notNullValue())
            .expectBody("created", notNullValue())
            .build();

    public static ResponseSpecification successfulRegistrationResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .build();


}
