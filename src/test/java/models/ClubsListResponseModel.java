package models;

import java.util.List;

// Основной ответ клубов с пагинацией
public record ClubsListResponseModel(
        Integer count,
        String next,
        String previous,
        List<ClubModel> results
) {}

