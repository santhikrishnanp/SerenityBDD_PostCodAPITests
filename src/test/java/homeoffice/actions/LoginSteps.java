package homeoffice.actions;

import homeoffice.PageObject.SauceDemoHome;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;
import static org.junit.Assert.assertEquals;
public class LoginSteps extends UIInteractionSteps {

    SauceDemoHome sauceDemoHome;

    @Step("The user enters their credentials '{0}' '{1}'")
    public void enterCredentials(String strUserName, String strPassword){
        sauceDemoHome = new SauceDemoHome(getDriver());
        sauceDemoHome.login(strUserName, strPassword);
    }

    @Step("User verifies URL path '{0}'")
    public void verifyURL(String strUrlActual){
        String url = getDriver().getCurrentUrl();
        assertEquals(url,strUrlActual);
    }
    @Step("The user enters wrong credentials and verifies the error message '{0}'")
    public void verifyErrorMsg(String errorMessage) {
        sauceDemoHome = new SauceDemoHome(getDriver());
        //String errorInline = find(SauceDemoHome.CREDENTIALS_ERROR_MSG).getText();
        assertEquals(sauceDemoHome.getErrorMessage(),errorMessage);
    }
}
