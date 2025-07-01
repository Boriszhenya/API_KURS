import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    @Test
    public void testRestAssured() {
        Map<String, Object> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();


        System.out.println("Pretty text: ");
        response.prettyPrint();

        System.out.println("Headers ");
        Headers responsHeaders = response.getHeaders();
        System.out.println(responsHeaders);


        System.out.println("Cookies ");
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);

        String cookieValue = responseCookies.get("auth_cookie");
        System.out.println(cookieValue);

    }
}
