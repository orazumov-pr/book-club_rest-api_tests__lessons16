package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.notNullValue;

public class ClubsSpec {

    // Успешное получение списка клубов
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

    // Успешное удаление клуба
    public static ResponseSpecification successfulClubDeleteResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

}