package models;

public record ReviewModel(
        Integer id,
        Integer club,
        Integer userId,
        String username,
        String review,
        Integer assessment,
        Integer readPages,
        String created,
        String modified
) {}
