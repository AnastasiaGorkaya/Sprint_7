import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Courier;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class CourierLoginTest {
    public static final String EMPTY_LOGIN_OR_PASSWORD_LOGIN_MESSAGE = "Недостаточно данных для входа";
    public static final String NOT_FOUND_LOGIN_COURIER_MESSAGE = "Учетная запись не найдена";

    private CourierClient courierClient;
    private Courier courier;
    private Courier notExistCourier = CourierGenerator.getRandom();
    private int courierId = -1;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void tearDown() {
        if (courierId != -1) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверяется возможность авторизации курьера")
    public void courierCanBeAuthorized() {
        ValidatableResponse createCourierResponse = courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertEquals(200, statusCode);
        assertThat(courierId, notNullValue());
        assertThat(courierId, Matchers.greaterThan(0));
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Проверяется невозможность авторизации курьера")
    public void courierAuthWithoutLoginFail() {
        ValidatableResponse createCourierResponse = courierClient.create(courier);
        ValidatableResponse validLoginResponse = courierClient.login(CourierCredentials.from(courier));
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier.setLogin(null)));

        int statusCode = loginResponse.extract().statusCode();
        courierId = validLoginResponse.extract().path("id");
        String responseMessage = loginResponse.extract().path("message");

        assertEquals(EMPTY_LOGIN_OR_PASSWORD_LOGIN_MESSAGE, responseMessage);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Проверяется невозможность авторизации курьера")
    public void courierAuthWithoutPasswordFail() {
        ValidatableResponse createCourierResponse = courierClient.create(courier);
        ValidatableResponse validLoginResponse = courierClient.login(CourierCredentials.from(courier));
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier.setPassword(null)));

        int statusCode = loginResponse.extract().statusCode();
        courierId = validLoginResponse.extract().path("id");

        assertEquals(504, statusCode);
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным логином")
    @Description("Проверяется невозможность авторизации курьера c некорректным логином")
    public void courierAuthIncorrectLoginFail() {
        ValidatableResponse createCourierResponse = courierClient.create(courier);
        ValidatableResponse validLoginResponse = courierClient.login(CourierCredentials.from(courier));
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier.setLogin("UNKNOWN_COURIER")));

        int statusCode = loginResponse.extract().statusCode();
        courierId = validLoginResponse.extract().path("id");
        String responseMessage = loginResponse.extract().path("message");

        assertEquals(NOT_FOUND_LOGIN_COURIER_MESSAGE, responseMessage);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным паролем")
    @Description("Проверяется невозможность авторизации курьера c некорректным паролем")
    public void courierAuthIncorrectPasswordFail() {
        ValidatableResponse createCourierResponse = courierClient.create(courier);
        ValidatableResponse validLoginResponse = courierClient.login(CourierCredentials.from(courier));
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier.setPassword("UNKNOWN_COURIER")));

        int statusCode = loginResponse.extract().statusCode();
        courierId = validLoginResponse.extract().path("id");
        String responseMessage = loginResponse.extract().path("message");

        assertEquals(NOT_FOUND_LOGIN_COURIER_MESSAGE, responseMessage);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Проверяется невозможность авторизации курьера с несуществующей парой логин+пароль")
    public void courierNotCreatedAuthFailed() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(notExistCourier));

        int statusCode = loginResponse.extract().statusCode();
        String responseMessage = loginResponse.extract().path("message");

        assertEquals(NOT_FOUND_LOGIN_COURIER_MESSAGE, responseMessage);
        assertEquals(404, statusCode);
    }
}