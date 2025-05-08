import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class courierCreateTests {
    private final String login = "SuperDupperCourier" + java.util.UUID.randomUUID(); //генерируем рандомного курьера
    private final String password = "NaGorshkeSidelK@rol" + java.util.UUID.randomUUID();//генерируем пароль
    private final String api = "/api/v1/courier";//апи
    CourierCreatorLogin courierCreatorLogin = new CourierCreatorLogin(login, password, login);

    @Before

    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

    }

    @Test
    @DisplayName("Создание курьера") // имя теста
    @Description("Выполнили пост запрос на создание курьера") // описание теста
    @Step("Создать курьера")
    public void courierCreatePositiveTest() {

        Response responseCourierFirst = //Позитивный сценарий
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierCreatorLogin)
                        .when()
                        .post(api);
        responseCourierFirst.then()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка повторного создания курьера") // имя теста
    @Description("Создали второго курьера с тем же логином проверили что он вернул 409 и текст сообщения в теле") // описание теста
    @Step("Проверить что курьер не создался повторно")
    public void courierCreateNegativeTest() {

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreatorLogin)
                .when()
                .post(api); //Создали курьера

        Response responseCourierSecond = //Негативный сценарий
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierCreatorLogin)
                        .when()
                        .post(api);
        responseCourierSecond.then()
                .assertThat()
                .statusCode(409)
                .body("code", equalTo(409))
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой.")); //Создали второго курьера с тем же логином проверили что он вернул 409 и текст сообщения в теле
    }

    @Test
    @DisplayName("создать курьера без логина и проверим что мы не можем это сделать") // имя теста
    @Description("Пытаемся создать курьера без логина и проверим что мы не можем это сделать , возвращаем 400 и проверяем сообщение Недостаточно данных для создания учетной записи") // описание теста
    @Step("Создание курьера без логина")
    public void courierCreateWithoutLoginTest() {
        CourierCreatorLogin courierWithoutLogin = new CourierCreatorLogin(null, password, login);
        Response responseCourierWithoutLogin = //Негативный сценарий
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierWithoutLogin)
                        .when()
                        .post(api);
        responseCourierWithoutLogin.then()
                .assertThat()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для создания учетной записи")); //Пытаемся создать курьера без логина и проверим что мы не можем это сделать , возвращаем 400 и проверяем сообщение Недостаточно данных для создания учетной записи
    }
}
