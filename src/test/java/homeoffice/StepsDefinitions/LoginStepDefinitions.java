package homeoffice.StepsDefinitions;

import homeoffice.actions.LoginSteps;
import homeoffice.actions.NavigateSteps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;



public class LoginStepDefinitions {

    @Steps
    LoginSteps sauceDemoHome;

    @Steps
    NavigateSteps browser;

    @Given("I open the browser on the SauceDemo page")
    public void user_opens_saucedemo_homepage() {
        browser.openSauceDemoPage();
    }

    @When("I enter the credentials {string} and {string}")
    public void user_enters_credentials(String strUserName, String strPassword) {
        sauceDemoHome.enterCredentials(strUserName, strPassword);
    }

    @Then("I should be redirected to the URL {string}")
    public void user_get_redirected_url(String urlPath) {
        sauceDemoHome.verifyURL(urlPath);
    }



    @Then("I should see the error message {string}")
    public void user_should_see_error_msg(String errorMsg) {
        sauceDemoHome.verifyErrorMsg(errorMsg);
    }
}
