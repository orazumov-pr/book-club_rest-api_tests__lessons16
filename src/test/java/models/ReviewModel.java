package models;

public record ReviewModel(
        Integer id,
        Integer club,
        UserShortModel user,
        String review,
        Integer assessment,
        Integer readPages,
        String created,
        String modified
) {}
