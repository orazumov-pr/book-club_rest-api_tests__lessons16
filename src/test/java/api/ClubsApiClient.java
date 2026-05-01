package api;

import models.ClubsErrorResponseModel;
import models.ClubsListResponseModel;
import models.CreateClubRequestModel;
import models.CreateClubResponseModel;

import static io.restassured.RestAssured.given;
import static specs.ClubsSpec.*;

public class ClubsApiClient {

    private static final String CLUBS_ENDPOINT = "/clubs/";
    private static final String CLUB_BY_ID_ENDPOINT = "/clubs/{id}/";


    // ========== GET МЕТОДЫ (ЧТЕНИЕ) ==========

    public ClubsListResponseModel getClubsList() {
        return given(clubsRequestSpec)
                .when()
                .get(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
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

    // ========== POST МЕТОДЫ (СОЗДАНИЕ) ==========

    public CreateClubResponseModel createClub(String accessToken, CreateClubRequestModel request) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .when()
                .post(CLUBS_ENDPOINT)
                .then()
                .spec(successfulClubCreateResponseSpec)
                .extract()
                .as(CreateClubResponseModel.class);
    }

    public ClubsErrorResponseModel createClubWithError(String accessToken, CreateClubRequestModel request, int expectedStatusCode) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .when()
                .post(CLUBS_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .as(ClubsErrorResponseModel.class);
    }



}
