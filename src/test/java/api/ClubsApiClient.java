package api;

import models.ClubsErrorResponseModel;
import models.ClubsListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.ClubsSpec.*;

public class ClubsApiClient {

    private static final String CLUBS_ENDPOINT = "/clubs/";
    private static final String CLUB_BY_ID_ENDPOINT = "/clubs/{id}/";

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


}
