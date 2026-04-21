package utils;

import com.github.javafaker.Faker;

public class LoginTestDataGenerator {

    private static final Faker faker = new Faker();

    // Валидные учетные данные
    public static final String VALID_USERNAME = "qaguru";
    public static final String VALID_PASSWORD = "qaguru123";

    // Невалидные учетные данные
    public static final String WRONG_PASSWORD = "qaguru1234";
    public static final String NON_EXISTENT_USERNAME = "nonexistent_user_12345";
    public static final String EMPTY_STRING = "";

    public static String generateRandomUsername() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.internet().password();
    }
}
