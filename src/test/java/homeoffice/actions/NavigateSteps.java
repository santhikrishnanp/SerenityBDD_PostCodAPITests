package homeoffice.actions;

import homeoffice.PageObject.SauceDemoHome;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;

public class NavigateSteps extends UIInteractionSteps {
    SauceDemoHome sauceDemo;
    @Step("The user opens the saucedemo page")
    public void openSauceDemoPage(){
        sauceDemo.open();
    }
}
