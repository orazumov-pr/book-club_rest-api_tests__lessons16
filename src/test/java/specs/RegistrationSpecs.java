package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;


public class RegistrationSpecs {

    public static RequestSpecification requestSpecification = with()
            .log().all()
            .contentType(ContentType.JSON)
            .basePath("/api/v1");

    public static ResponseSpecification error400ResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .build();
}
