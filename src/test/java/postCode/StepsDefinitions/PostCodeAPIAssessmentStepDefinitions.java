package postCode.StepsDefinitions;

import postCode.api.questions.ExtractCoordinates;
import postCode.api.questions.FindNearestPostcode;
import postCode.api.questions.ResponseStatus;
import postCode.api.tasks.CheckApiHealth;
import postCode.api.tasks.GetNearestPostcodesSteps;
import postCode.api.tasks.GetPostCodeDetailsSteps;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

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


    @Given("the API service is up & healthy")
    public void verifyAPIHealth() {
        actor.attemptsTo(CheckApiHealth.isApiUp());
        actor.should(seeThat(ResponseStatus.code(),equalTo(200)));
    }

    @When("I search the postcode {string}")
    public void lookupPostcode(String value){
        actor.attemptsTo(GetPostCodeDetailsSteps.forPostCode(value));

    }

    @Then("the api response status code should be {int}")
    public void verifyStatusCode(int expectedCode){
        actor.should(seeThat(ResponseStatus.code(),equalTo(expectedCode)));
    }

    @And("I extract the longitude and latitude from the response")
    public void extractTheCoordinates(){
        double[] coordinates = actor.asksFor(ExtractCoordinates.fromResponse());

        longitude = coordinates[0];
        latitude = coordinates[1];
        assertThat(longitude).isNotEqualTo(0.0);
        assertThat(latitude).isNotEqualTo(0.0);

    }

    @When("I lookup the nearest postcodes using the extracted coordinates")
    public void lookUpNearestPostcode(){
        actor.attemptsTo(GetNearestPostcodesSteps.usingCoordinates(longitude,latitude));

    }

    @Then("the nearest postcode returned should be {string}")
    public void verifyNearestPostCode(String value){
        String actualPostCode = actor.asksFor(FindNearestPostcode.fromResponse());
        assertThat(actualPostCode).as("Verify Nearest postcode from the coordinates").isEqualTo(value);
    }


}
