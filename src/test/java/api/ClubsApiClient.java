package api;

import models.*;

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
        var response = given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .when()
                .post(CLUBS_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract();

        try {
            return response.as(ClubsErrorResponseModel.class);
        } catch (Exception e) {
            // Если не получилось, создаем модель с detail из ответа
            String detail = response.path("detail");
            return new ClubsErrorResponseModel(detail, null, null, null, null);
        }
    }

    // ========== DELETE МЕТОДЫ ==========

    public void deleteClub(String accessToken, int clubId) {
        given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", clubId)
                .when()
                .delete(CLUB_BY_ID_ENDPOINT)
                .then()
                .spec(successfulClubDeleteResponseSpec);
    }

    public ClubsErrorResponseModel deleteClubWithError(String accessToken, int clubId, int expectedStatusCode) {
        var response = given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", clubId)
                .when()
                .delete(CLUB_BY_ID_ENDPOINT)
                .then()
                .statusCode(expectedStatusCode)
                .extract();

        try {
            return response.as(ClubsErrorResponseModel.class);
        } catch (Exception e) {
            String detail = response.path("detail");
            return new ClubsErrorResponseModel(detail, null, null, null, null);
        }
    }

    public void deleteClubWithoutToken(int clubId) {
        given(clubsRequestSpec)
                .pathParam("id", clubId)
                .when()
                .delete(CLUB_BY_ID_ENDPOINT)
                .then()
                .spec(unauthorizedResponseSpec);
    }
}