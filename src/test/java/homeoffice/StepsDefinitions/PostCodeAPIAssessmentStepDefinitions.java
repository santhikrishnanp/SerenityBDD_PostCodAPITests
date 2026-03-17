package homeoffice.StepsDefinitions;

import homeoffice.api.questions.ExtractCoordinates;
import homeoffice.api.questions.FindNearestPostcode;
import homeoffice.api.tasks.CheckApiHealth;
import homeoffice.api.tasks.GetNearestPostcodesSteps;
import homeoffice.api.tasks.GetPostCodeDetailsSteps;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static org.assertj.core.api.Assertions.assertThat;

public class PostCodeAPIAssessmentStepDefinitions {

    private  final Actor actor = Actor.named("Test Engineer");
    private double longitude;
    private double latitude;

    @Before
    public void configure(){
        String baseUrl = WebDriverConfiguredEnvironment.getEnvironmentVariables()
                .getProperty("serenity.environments.default.postcode.url");
        System.out.println("base url is "+baseUrl);
        actor.can(CallAnApi.at(baseUrl));
    }

//    private String getPostCodeUrl() {
//        return WebDriverConfiguredEnvironment.getEnvironmentVariables()
//                .getProperty("serenity.environments.default.postcode.url");
//    }


    @Given("the API service is up & healthy")
    public void verifyAPIHealth() {
        actor.attemptsTo(CheckApiHealth.isApiUp());
        assertThat(SerenityRest.lastResponse().statusCode()).isEqualTo(200);
    }

    @When("I search the postcode")
    public void lookupPostcode(io.cucumber.datatable.DataTable table){
        String postcode = table.asList().get(1);

        actor.attemptsTo(GetPostCodeDetailsSteps.forPostCode(postcode));

    }

    @Then("the api response status code should be {int}")
    public void verifyStatusCode(int expectedCode){
        int code = SerenityRest.lastResponse().statusCode();

        assertThat(code).as("Verify API response status code").isEqualTo(expectedCode);

    }

    @And("I extract the longitude and latitude from the response")
    public void extractTheCoordinates(){
        double[] coorindates = actor.asksFor(ExtractCoordinates.fromResponse());

        longitude = coorindates[0];
        latitude = coorindates[1];
        assertThat(longitude).isNotEqualTo(0.0);
        assertThat(latitude).isNotEqualTo(0.0);

    }

    @When("I lookup the nearest postcodes using the extracted coordinates")
    public void lookUpNearestPostcode(){
        actor.attemptsTo(GetNearestPostcodesSteps.usingCoordinates(longitude,latitude));

    }

    @Then("the nearest postcode returned should be SW1A 1AA")
    public void verifyNearestPostCode(){
        String actualPostCode = actor.asksFor(FindNearestPostcode.fromResponse());
        assertThat(actualPostCode).as("Verify Nearest postcode from the coordinates").isEqualTo("SW1A 1AA");
    }


}
