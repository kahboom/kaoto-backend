package io.zimara.backend.api;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GetViewsTest {

    @LoggerName("GetViewsTest")
    Logger log;

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/view")
          .then()
             .statusCode(200)
             .body(is("[{\"name\":\"test view\",\"steps\":[{\"ID\":\"kamelet\",\"icon\":\"icon.png\",\"id\":\"kamelet\",\"name\":\"kamelet\",\"subType\":\"KAMELET\",\"type\":\"CONNECTOR\"},{\"ID\":\"kamelet2\",\"icon\":\"icon.png\",\"id\":\"kamelet2\",\"name\":\"kamelet2\",\"subType\":\"KAMELET\",\"type\":\"CONNECTOR\"}],\"type\":\"INTEGRATION\"}]"));
    }

}