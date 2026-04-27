package api;

/**
 * Общий API-клиент — единая точка доступа к клиентам эндпоинтов.
 */
public class ApiClient {

    public final AuthApiClient auth = new AuthApiClient();
    public final UserApi users = new UserApi();

}
