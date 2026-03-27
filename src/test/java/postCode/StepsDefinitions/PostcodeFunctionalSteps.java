package postCode.StepsDefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Step definitions demonstrating basic Java 8 features
 * Tests Stream API, Optional, and Collectors
 */
public class PostcodeFunctionalSteps {

    private List<Response> responses = new ArrayList<>();
    private List<Map<String, Object>> processedResults = new ArrayList<>();
    private Map<String, List<String>> groupedData = new HashMap<>();
    private final String BASE_URL = getPostCodeAPI();

    private String getPostCodeAPI() {
        return net.serenitybdd.core.environment.WebDriverConfiguredEnvironment.getEnvironmentVariables()
                .getProperty("serenity.environments.default.postcode.api.endpoint");
    }

    /**
     * Helper method to safely convert Map<Object,Object> to Map<String,Object>
     */
    private Map<String, Object> convertMap(Map<Object, Object> originalMap) {
        Map<String, Object> converted = new HashMap<>();
        originalMap.forEach((key, value) -> converted.put(String.valueOf(key), value));
        return converted;
    }

    // ==================== Scenario 1: Stream Filtering ====================

    @When("I fetch multiple postcodes for analysis:")
    public void fetchMultiplePostcodesForAnalysis(List<String> postcodes) {
        responses = postcodes.stream()
                .map(postcode -> SerenityRest.given().get(BASE_URL + postcode))
                .collect(Collectors.toList());
    }

    @Then("I should filter postcodes by region {string}")
    public void filterPostcodesByRegion(String targetRegion) {
        processedResults = responses.stream()
                .filter(response -> response.getStatusCode() == 200)
                .map(response -> convertMap(response.jsonPath().getMap("result")))
                .filter(result -> targetRegion.equals(result.get("region")))
                .collect(Collectors.toList());
    }

    @And("the result should contain {int} postcodes")
    public void verifyResultCount(int expectedCount) {
        assertThat("Filtered result count mismatch", 
            processedResults.size(), equalTo(expectedCount));
    }

    // ==================== Scenario 2: Collectors & Grouping ====================

    @When("I fetch postcodes across different regions:")
    public void fetchPostcodesAcrossDifferentRegions(List<String> postcodes) {
        responses = postcodes.stream()
                .map(postcode -> SerenityRest.given().get(BASE_URL + postcode))
                .filter(response -> response.getStatusCode() == 200)
                .collect(Collectors.toList());
    }

    @Then("I should group postcodes by region")
    public void groupPostcodesByRegion() {
        groupedData = responses.stream()
                .map(response -> convertMap(response.jsonPath().getMap("result")))
                .collect(Collectors.groupingBy(
                    result -> String.valueOf(result.get("region")),
                    Collectors.mapping(
                        result -> String.valueOf(result.get("postcode")),
                        Collectors.toList()
                    )
                ));
    }

    @And("count postcodes per region")
    public void countPostcodesPerRegion() {
        Map<String, Long> regionCounts = responses.stream()
                .map(response -> convertMap(response.jsonPath().getMap("result")))
                .collect(Collectors.groupingBy(
                    result -> String.valueOf(result.get("region")),
                    Collectors.counting()
                ));
        
        // Verify we have counts
        assertThat("Region counts should be calculated", 
            regionCounts.size(), greaterThan(0));
    }

    @And("the region {string} should have {int} postcodes")
    public void verifyRegionPostcodeCount(String region, int expectedCount) {
        assertThat("Region postcode count mismatch",
            groupedData.get(region).size(), equalTo(expectedCount));
    }

    // ==================== Scenario 3: Optional & Null Handling ====================

    @When("I lookup postcodes with potential missing data:")
    public void lookupPostcodesWithPotentialMissingData(List<String> postcodes) {
        responses = postcodes.stream()
                .map(postcode -> {
                    try {
                        return SerenityRest.given().get(BASE_URL + postcode);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    @Then("I should extract only valid responses using Optional")
    public void extractValidResponsesUsingOptional() {
        processedResults = responses.stream()
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(response -> response.getStatusCode() == 200)
                .map(response -> convertMap(response.jsonPath().getMap("result")))
                .collect(Collectors.toList());
    }

    @And("count the valid postcodes as {int}")
    public void countValidPostcodes(int expectedCount) {
        long validCount = processedResults.stream()
                .filter(Objects::nonNull)
                .count();
        assertThat("Valid postcode count mismatch", 
            (int) validCount, equalTo(expectedCount));
    }
}
