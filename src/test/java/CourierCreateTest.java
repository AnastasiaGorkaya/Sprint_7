import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierCreateTest {
    public static final String EMPTY_LOGIN_OR_PASSWORD_CREATE_MESSAGE = "Недостаточно данных для создания учетной записи";
    public static final String LOGIN_ALREADY_USED_RESPONSE_MESSAGE = "Этот логин уже используется. Попробуйте другой.";

    private CourierClient cc;
    private Courier courier;
    private Courier emptyLoginCourier;
    private Courier emptyPasswordCourier;
    private Integer courierId;

    @Before
    public void setUp() {
        cc = new CourierClient();
        courier = CourierGenerator.getRandom();
        emptyLoginCourier = CourierGenerator.getRandomWithoutLogin();
        emptyPasswordCourier = CourierGenerator.getRandomWithoutPassword();
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            cc.delete(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверяется возможность создания курьера")
    public void courierCanBeCreated() {
        ValidatableResponse createCourierResponse = cc.create(courier);

        int statusCode = createCourierResponse.extract().statusCode();
        boolean isCourierCreated = createCourierResponse.extract().path("ok");

        assertEquals(201, statusCode);
        assertTrue(isCourierCreated);

        ValidatableResponse loginResponse = cc.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверяется ошибка при попытке создать курьера без обязательного поля login")
    public void createCourierNoLoginFail() {
        ValidatableResponse createCourierResponse = cc.create(emptyLoginCourier);

        int statusCode = createCourierResponse.extract().statusCode();
        String responseMessage = createCourierResponse.extract().path("message");

        assertEquals(400, statusCode);
        assertEquals(EMPTY_LOGIN_OR_PASSWORD_CREATE_MESSAGE, responseMessage);

    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверяется ошибка при попытке создать курьера без обязательного поля password")
    public void createCourierNoPasswordFail() {
        ValidatableResponse createCourierResponse = cc.create(emptyPasswordCourier);

        int statusCode = createCourierResponse.extract().statusCode();
        String responseMessage = createCourierResponse.extract().path("message");

        assertEquals(400, statusCode);
        assertEquals(EMPTY_LOGIN_OR_PASSWORD_CREATE_MESSAGE, responseMessage);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверяется невозможность создания двух одинаковых курьеров")
    public void createTwoEqualCouriersFail() {
        ValidatableResponse createCourierResponseFirst = cc.create(courier);
        ValidatableResponse createCourierResponseSecond = cc.create(courier);

        int statusCode = createCourierResponseSecond.extract().statusCode();
        String responseMessage = createCourierResponseSecond.extract().path("message");

        assertEquals(409, statusCode);
        assertEquals(LOGIN_ALREADY_USED_RESPONSE_MESSAGE, responseMessage);

        ValidatableResponse loginResponse = cc.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }
}