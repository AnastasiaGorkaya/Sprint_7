import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final int rentTime;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private OrderClient orderClient;
    private Order order;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Создание заказа с цветом: {7}")
    public static Object[][] createOrderData() {
        return new Object[][]{
                {"Алексей", "Иванов", "Московская 15", "Чкаловская", "89222049191", 60, "2023-05-15", "Серый и черный", new String[]{"GREY", "BLACK"}},
                {"Алексей", "Иванов", "Московская 15", "Чкаловская", "89222049191", 60, "2023-05-15", "Черный и серый", new String[]{"BLACK", "GREY"}},
                {"Алексей", "Иванов", "Московская 15", "Чкаловская", "89222049191", 60, "2023-05-15", "Черный", new String[]{"BLACK"}},
                {"Алексей", "Иванов", "Московская 15", "Чкаловская", "89222049191", 60, "2023-05-15", "Серый", new String[]{"GREY"}},
                {"Алексей", "Иванов", "Московская 15", "Чкаловская", "89222049191", 60, "2023-05-15", "Без цвета", null},
        };
    }

    @Before
    public void setUp(){
        orderClient = new OrderClient();
        order = new Order(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверяется возможность создания заказа")
    public void orderCanBeCreated() {
        ValidatableResponse createOrderResponse = orderClient.create(order);

        int statusCode = createOrderResponse.extract().statusCode();
        int orderId = createOrderResponse.extract().path("track");

        assertThat(orderId, notNullValue());
        assertThat(orderId, Matchers.greaterThan(0));
        assertEquals(201, statusCode);
    }
}