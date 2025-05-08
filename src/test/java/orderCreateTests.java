import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class orderCreateTests {
    private final String[] color;
    private final String firstname;
    private final String lastname;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;


    public orderCreateTests(String[] color) {
        this.color = color;
        this.firstname = "volodka" + UUID.randomUUID();
        this.lastname = "molodec" + UUID.randomUUID();
        this.address = "City Of Moscow";
        this.metroStation = "4";
        this.phone = String.valueOf((new Random().nextInt(7) + 1) * 100);
        this.rentTime = 10;
        this.deliveryDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.comment = "Привезите сегодня";
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {new String[]{"BLACK"}}, // Черный
                {new String[]{"GREY"}}, // Серый
                {new String[]{"BLACK", "GREY"}}, // Черный и Серый
                {new String[]{""}}  // Прозрачный))
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание заказа и проверка на цвет" )
    @Description("Тестирование разных сценариев по заказу")
    @Step("Создание заказа и его проверка на цвет")
    public void createOrderTest() {

        OrderCreator orderCreator = new OrderCreator(firstname, lastname, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Response response = given()
                .header("Content-type", "application/json")
                .body(orderCreator)
                .when()
                .post("/api/v1/orders");

        response.then()
                .assertThat()
                .statusCode(201)
                .body("track", notNullValue());
    }





}