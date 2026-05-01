package tests;

import api.ApiClient;
import models.ClubModel;
import models.ClubsErrorResponseModel;
import models.ClubsListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static specs.ClubsSpec.clubsRequestSpec;

public class ClubsReadTests extends TestBase {

    private final ApiClient api = new ApiClient();

    // ================ ПОЗИТИВНЫЕ ТЕСТЫ =================

    @Test
    @DisplayName("Получение списка клубов должно вернуть 200 и непустой список")
    void getClubsListReturn200() {
        ClubsListResponseModel response = api.club.getClubsList();

        assertThat(response.count()).isGreaterThan(0);
        assertThat(response.results()).isNotNull();
        assertThat(response.results()).isNotEmpty();
    }

    @Test
    @DisplayName("Получение списка клубов должно содержать валидные данные в первом клубе")
    void getClubsListValidClubData() {
        ClubsListResponseModel response = api.club.getClubsList();
        ClubModel firstClub = response.results().get(0);

        assertThat(firstClub.id()).isNotNull();
        assertThat(firstClub.bookTitle()).isNotNull();
        assertThat(firstClub.bookAuthors()).isNotNull();
        assertThat(firstClub.publicationYear()).isNotNull();
        assertThat(firstClub.owner()).isNotNull();
        assertThat(firstClub.members()).isNotNull();
        assertThat(firstClub.created()).isNotNull();
    }

    // ================ НЕГАТИВНЫЕ ТЕСТЫ =================

    @Test
    @DisplayName("Получение клуба с нулевым ID должно вернуть 404")
    void getClubWithZeroIdReturn404() {
        int zeroId = 0;

        ClubsErrorResponseModel response = api.club.getClubByIdWithError(zeroId, 404);

        assertThat(response.detail()).isNotNull();
    }

    @Test
    @DisplayName("Ответ со списком клубов должен содержать обязательные поля")
    void clubsListRequiredFields() {
        String response = given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .body("$", hasKey("count"))
                .body("$", hasKey("results"))
                .body("$", hasKey("next"))
                .body("$", hasKey("previous"))
                .extract()
                .asString();

        assertThat(response).isNotNull();
    }


}
