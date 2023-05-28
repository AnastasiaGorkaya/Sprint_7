import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OrderGetListTest {
    private OrderClient orderClient;
    private List<Order> orderList;

    @Before
    public void setUp(){
        orderClient = new OrderClient();
        orderList = new ArrayList<>();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяется возможность получения спика заказов")
    public void orderListCanBeReturnedSuccess() {
        ValidatableResponse getOrdersResponse = orderClient.getOrders();

        int statusCode = getOrdersResponse.extract().statusCode();
        orderList = getOrdersResponse.extract().path("orders");

        assertThat(orderList.size(), greaterThan(0));
        assertEquals(200, statusCode);
    }
}