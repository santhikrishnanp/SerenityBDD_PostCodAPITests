package homeoffice.StepsDefinitions;

import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.serenitybdd.rest.SerenityRest;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import java.util.*;
import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.Matchers.*;

public class PostcodeAPISteps {
    private Response response;
    private final Map<String, Object> testContext = new HashMap<>();
    private final String BASE_URL = getPostCodeAPI();

    private String getPostCodeUrl() {
        return WebDriverConfiguredEnvironment.getEnvironmentVariables()
                .getProperty("serenity.environments.default.postcode.url");
    }

    private String getPostCodeAPI() {
        return WebDriverConfiguredEnvironment.getEnvironmentVariables()
                .getProperty("serenity.environments.default.postcode.api.endpoint");
    }

    @Given("the API service is healthy")
    public void verifyAPIHealth() {
        Response healthCheck = SerenityRest.given()
                .get(getPostCodeUrl());
        Assert.assertEquals(200, healthCheck.getStatusCode());
    }

    @When("I lookup the postcode {string}")
    public void lookupPostcode(String postcode) {
        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .get(BASE_URL + postcode);
        testContext.put("last_response", response);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @And("the response should contain the following fields:")
    public void verifyResponseFields(DataTable dataTable) {
        List<Map<String, String>> fields = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> field : fields) {
            String fieldPath = "result." + field.get("field");
            String expectedValue = field.get("value");

            response.then()
                    .body(fieldPath, is(expectedValue));
        }
    }

    @And("all coordinate values should be within UK boundaries")
    public void verifyCoordinateBoundaries() {
        // Extract coordinates as Float to match API response type
        Float latitude = response.jsonPath().getFloat("result.latitude");
        Float longitude = response.jsonPath().getFloat("result.longitude");

        Assert.assertNotNull("Latitude should not be null", latitude);
        Assert.assertNotNull("Longitude should not be null", longitude);

        // UK boundary checks using float values
        Assert.assertTrue("Latitude out of UK bounds", latitude > 49.8f && latitude < 60.9f);
        Assert.assertTrue("Longitude out of UK bounds", longitude > -8.74f && longitude < 1.96f);
    }

    @When("I perform a bulk lookup with the following postcodes:")
    public void bulkLookupPostcodes(List<String> postcodes) {
        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("postcodes", postcodes);

        Instant startTime = Instant.now();
        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(BASE_URL);
        testContext.put("request_start_time", startTime);
        testContext.put("bulk_response", response);
    }

    @And("all responses should be received within {int} milliseconds")
    public void verifyResponseTime(int maxMillis) {
        Instant startTime = (Instant) testContext.get("request_start_time");
        Duration duration = Duration.between(startTime, Instant.now());
        Assert.assertTrue(
                String.format("Response time %d ms exceeded maximum %d ms",
                        duration.toMillis(), maxMillis),
                duration.toMillis() < maxMillis
        );
    }

    @And("each postcode result should contain required fields")
    public void verifyRequiredFields() {
        response.then()
                .body("status", equalTo(200))
                .body("result.size()", equalTo(3))
                .body("result.every { it.result != null }", is(true))
                .body("result.every { it.result.postcode != null }", is(true))
                .body("result.every { it.result.country != null }", is(true))
                .body("result.every { it.result.region != null }", is(true));

        // Additional detailed validation
        List<Map<String, Object>> results = response.jsonPath().getList("result");
        for (Map<String, Object> result : results) {
            Map<String, Object> postcodeResult = (Map<String, Object>) result.get("result");
            Assert.assertNotNull("Postcode result should not be null", postcodeResult);
            Assert.assertNotNull("Postcode should not be null", postcodeResult.get("postcode"));
            Assert.assertNotNull("Country should not be null", postcodeResult.get("country"));
            Assert.assertNotNull("Region should not be null", postcodeResult.get("region"));
        }
    }

    @When("I search for nearest postcodes using its coordinates")
    public void searchNearestPostcodes() {
        Response lastResponse = (Response) testContext.get("last_response");
        Float latitude = lastResponse.jsonPath().getFloat("result.latitude");
        Float longitude = lastResponse.jsonPath().getFloat("result.longitude");

        response = SerenityRest.given()
                .contentType(ContentType.JSON)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("radius", 1000)
                .get(BASE_URL);

        testContext.put("nearest_response", response);
    }

    @And("the nearest postcodes should be within {int} meters")
    public void verifyNearestPostcodesDistance(int maxDistance) {
        response.then()
                .body("result", everyItem(hasKey("distance")))
                .body("result.findAll { result -> result.distance <= " + maxDistance + "}.size()",
                        equalTo(response.path("result.size()")));
    }

    @And("results should be ordered by distance")
    public void verifyDistanceOrdering() {
        // Use Float instead of Double
        List<Float> distances = response.jsonPath().getList("result.distance", Float.class);

        for (int i = 0; i < distances.size() - 1; i++) {
            Assert.assertTrue("Results not ordered by distance",
                    distances.get(i) <= distances.get(i + 1));
        }
    }


    @And("the error message should contain {string}")
    public void verifyErrorMessage(String errorText) {
        response.then()
                .body("error", containsString(errorText));
    }

    @Then("verify administrative hierarchy:")
    public void verifyAdministrativeHierarchy(DataTable hierarchyTable) {
        List<Map<String, String>> hierarchyLevels = hierarchyTable.asMaps();

        for (Map<String, String> level : hierarchyLevels) {
            String field = level.get("level");
            String expectedValue = level.get("value");

            response.then()
                    .body("result." + field, equalTo(expectedValue));
        }
    }


}