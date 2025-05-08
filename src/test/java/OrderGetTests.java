import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class OrderGetTests {
    private final int limit = 30;
    private final int page = 0;
    private final String nearestStation = "[\"110\"]";
    OrderFilters orderFilters = new OrderFilters(limit,page,nearestStation);
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем что в тело ответа возвращается список заказов.")
    @Step("Получаем список заказов и проверяем что в тело ответа возвращается список заказаов")
    public void orderCreatorFilters() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(orderFilters)
                .when()
                .get("/api/v1/orders");
        response.then().statusCode(200);
        List<Map<String, Object>> orders = response.jsonPath().getList("orders");
        assertEquals(limit, orders.size());   //Проверяем что в тело ответа возвращается список заказов.
    }
}
