import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NegativeUserAuthTest {


    @Test
    public void testAuthUser() {
        Map<String, Object> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

        assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code ");
        assertTrue(cookies.containsKey("auth_sid"), "Cookie auth_sid not found");
        assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Header x-csrf-token not found");
        assertTrue(responseGetAuth.jsonPath().getInt("user_id") > 0, "User id not found");

        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token"))
                .cookies("auth_sid", responseGetAuth.getCookie("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnAuth > 0, "Unexpected user id " + userIdOnAuth);

        assertEquals(
                userIdOnAuth,
                userIdOnCheck,
                "User ids are different"
        );


    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    void testNegativeAuthUser(String condition) {
        Map<String, Object> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookies.get("auth_sid"));

        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", headers.get("x-csrf-token"));
        } else {
            throw new IllegalArgumentException("Unexpected condition: " + condition);
        }
        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(
                0,
                responseForCheck.getInt("user_id"),
                "User id should be 0"
        );
    }

}

