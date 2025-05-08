import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class courierLoginTests {
    private final static String login = "SuperDupperCourier"+java.util.UUID.randomUUID(); //генерируем рандомного курьера
    private final static String password = "NaGorshkeSidelK@rol"+java.util.UUID.randomUUID(); //пароль
    private final String api = "/api/v1/courier/login";//апи
    CourierCreatorLogin courierCreatorLogin = new CourierCreatorLogin(login, password, login);
    CourierCreatorLogin courierLogin = new CourierCreatorLogin(login, password);
    @Before

    public void setUp() { //Мы создаем курьера , я тут предусмотрел если данные удалят из бд то авторизацию точно проверим
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreatorLogin)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Create Courier/api/v1/courier") // имя теста
    @Description("Я объединил авторизацию и удаление вместе так как id возвращается после авторизации") // описание теста
    @Step("Проверяем авторизацию и удаление курьера")
    //Я объединил авторизацию и удаление вместе так как id возвращается после авторизации
    public void courierLoginTestAndDelete()  {
        Response courierLoginTest = //Позитивный сценарий
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post(api);
        courierLoginTest.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());//Проверили что статус код 200 и ид возвращается не пустым
        int courierId = courierLoginTest.then()
                .extract()
                .path("id"); //Извлекаем переменную ID курьера
        given()
                .header("Content-Type", "application/json")
                .when()
                .delete(api + "/" + courierId);//Удаляем курьера

        given()
                .header("Content-Type", "application/json")
                .when()
                .get(api + "/" + courierId)
                .then()
                .statusCode(404); // Проверяем что курьера нет статус код 404.

    }



    @Test
    @DisplayName("Учетная запись не найдена") // имя теста
    @Description("Проверили что учетная запись не найдена и статус код = 404") // описание теста
    @Step("Выполняем проверку что нет такой учетной записи")
    public void incorrectCourierLogoPassTest() {
        CourierCreatorLogin courierLogin = new CourierCreatorLogin("amazing"+java.util.UUID.randomUUID(), "amazing"+java.util.UUID.randomUUID());
        Response incorrectCourierLogoPassTest = //Позитивный сценарий
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post(api);
        incorrectCourierLogoPassTest.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));//Проверили что учетная запись не найдена и статус код = 404
    }

    @Test
    @DisplayName("Негативный сценарий проверки отсутствия логина и пароля в теле запроса") // имя теста
    @Description("Проверили что возвращается 400 и сообщение Недостаточно данных для входа") // описание теста
    @Step("Проверили отсутствие логина и пароля в бади")
    public void incorrectCourierMissingPasswordTest() {
        CourierCreatorLogin courierLogin = new CourierCreatorLogin("","");
        Response incorrectCourierMissingPasswordTest = //Позитивный сценарий
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post(api);
        incorrectCourierMissingPasswordTest.then()
                .assertThat()
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для входа"));//Проверили что возвращается 400 и сообщение Недостаточно данных для входа
    }

}
