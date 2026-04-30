package api;


import models.clubs.ClubsErrorResponseModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.ClubModel;
import specs.ClubsSpec;

import static io.restassured.RestAssured.given;
import static specs.ClubsSpec.*;

public class ClubsApiClient {

    private static final String CLUBS_ENDPOINT = "/clubs/";
    private static final String CLUB_BY_ID_ENDPOINT = "/clubs/{id}/";

    // ========== ПОЛУЧЕНИЕ СПИСКА КЛУБОВ ==========

    public ClubsListResponseModel getClubsList() {
        return given(clubsRequestSpec)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubsListResponseModel getClubsListWithPagination(int page, int pageSize) {
        return given(clubsRequestSpec)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubsListResponseModel getClubsListWithSearch(String searchQuery) {
        return given(clubsRequestSpec)
                .queryParam("search", searchQuery)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubsListResponseModel getClubsListWithMembership(String membership) {
        return given(clubsRequestSpec)
                .queryParam("membership", membership)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubsListResponseModel getClubsListWithAllFilters(int page, int pageSize, String search, String membership) {
        return given(clubsRequestSpec)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .queryParam("search", search)
                .queryParam("membership", membership)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    // ========== ПОЛУЧЕНИЕ КОНКРЕТНОГО КЛУБА ==========

    public ClubModel getClubById(int clubId) {
        return given(clubsRequestSpec)
                .pathParam("id", clubId)
                .when()
                .get(CLUB_BY_ID_ENDPOINT)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    // ========== НЕГАТИВНЫЕ МЕТОДЫ ==========

    public ClubsErrorResponseModel getClubsListWithError(int expectedStatusCode) {
        return given(clubsRequestSpec)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .as(ClubsErrorResponseModel.class);
    }

    public ClubsErrorResponseModel getClubByIdWithError(int clubId, int expectedStatusCode) {
        return given(clubsRequestSpec)
                .pathParam("id", clubId)
                .when()
                .get(CLUB_BY_ID_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .as(ClubsErrorResponseModel.class);
    }

    public void getClubsListWithInvalidMethod(String method) {
        given(clubsRequestSpec)
                .when()
                .request(method, CLUBS_ENDPOINT)
                .then()
                .spec(methodNotAllowedResponseSpec);
    }

    public void getClubsListWithInvalidContentType(String contentType) {
        given()
                .log().all()
                .contentType(contentType)
                .basePath("/api/v1")
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .log().all()
                .statusCode(415);
    }
}
