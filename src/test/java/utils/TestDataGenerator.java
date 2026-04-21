package utils;

import com.github.javafaker.Faker;

public class TestDataGenerator {

    private static final Faker faker = new Faker();

    public static String generateValidUsername() {
        return faker.name().username();
    }

    public static String generateValidPassword() {
        return faker.internet().password(8, 20);
    }
}