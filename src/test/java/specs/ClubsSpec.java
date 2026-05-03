package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.notNullValue;

public class ClubsSpec {

    public static RequestSpecification clubsRequestSpec = with()
            .log().all()
            .contentType(ContentType.JSON)
            .baseUri("https://book-club.qa.guru")  // Явно указываем URI
            .basePath("/api/v1");

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

    // Успешный ответ с конкретным клубом
    public static ResponseSpecification successfulClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("id", notNullValue())
            .expectBody("bookTitle", notNullValue())
            .expectBody("owner", notNullValue())
            .build();

    // Успешное удаление клуба
    public static ResponseSpecification successfulClubDeleteResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

    // Ошибка валидации
    public static ResponseSpecification badRequestResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .build();

    // Неавторизованный доступ
    public static ResponseSpecification unauthorizedResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .build();

    // Не найдено
    public static ResponseSpecification notFoundResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(404)
            .build();
}