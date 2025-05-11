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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class getOrderFilterTest {
    private final int limit = 30;
    private final int page = 0;
    private final String nearestStation = "[\"110\"]";
    private final String order = "/api/v1/orders?limit=" + limit + "&page=" + page + "&nearestStation=" + nearestStation;
    private int statusCode = 200;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получение списка заказов по фильтру")
    @Description("Проверяем что в тело ответа возвращается список заказов по фильтру")
    @Step("Получаем список заказов и проверяем что в тело ответа возвращается список заказов + некоторые проверки")
    public void orderCreatorFilters() {
        Response response = given()
                .get(order);
                response.then().statusCode(statusCode);
        List<Map<String, Object>> ordersFilters = response.jsonPath().getList("orders"); // Проверяем наличие списка заказов в ответе
        assertThat(ordersFilters.size(), equalTo(limit)); //Проверяем что количество заказов на странице = 30
        assertThat(ordersFilters, not(empty())); // Проверяем, что список заказов не пустой
    }
}
