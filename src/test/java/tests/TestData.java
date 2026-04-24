package tests;

public class TestData {

    // Существующий пользователь
    public static final String LOGIN_USERNAME = "user8";
    public static final String LOGIN_PASSWORD = "user8";

    // Невалидные данные для негативных тестов
    public static final String WRONG_PASSWORD = "qaguru1234";
    public static final String EMPTY_USERNAME = "";
    public static final String NON_EXISTENT_USERNAME = "nonexistent_user_12345";

    // Токены
    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String INVALID_REFRESH_TOKEN = "invalid.token.here";

    // Сообщения об ошибках
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid username or password.";

    // Генерация уникальных данных
    public static String generateUniqueUsername() {
        return "test_user_" + System.currentTimeMillis();
    }

    public static String generateUniquePassword() {
        return "test_pass_" + System.currentTimeMillis();
    }


//    public static final String REGISTRATION_IP_REGEXP =
//            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
//                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

}
