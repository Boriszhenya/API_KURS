import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured() {
//Map<String, Object> body = new HashMap<>();
//body.put("param1", "value1");
//body.put("param2", "value2");
        Map<String, Object> headers = new HashMap<>();
        headers.put("myHeaders1", "myValue1");
        headers.put("myHeaders2", "myValue2");

        Response response = RestAssured
                .given()
                .redirects().
                follow(false)
                .headers(headers)
                .when()
                //.body("param1=value1&param2=value2")
              //  .body(body)
                //.post("https://playground.learnqa.ru/api/check_type")
                .get("https://playground.learnqa.ru/api/get_303")

                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
      response.prettyPrint();
    String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);


}}