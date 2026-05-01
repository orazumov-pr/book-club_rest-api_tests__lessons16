package models;


public record ClubsErrorResponseModel(
        String detail,
        String[] bookTitle,
        String[] bookAuthors,
        String[] description,
        String[] telegramChatLink
) {}
