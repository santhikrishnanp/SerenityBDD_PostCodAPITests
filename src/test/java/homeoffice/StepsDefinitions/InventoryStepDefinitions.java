package homeoffice.StepsDefinitions;

import homeoffice.PageObject.SauceDemoInventory;
import homeoffice.actions.NavigateSteps;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

import static net.serenitybdd.core.Serenity.getDriver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InventoryStepDefinitions {

     final String INVENTORY = "innventory";
     final String PRICE_LOW_TO_HIGH = "price low to high";

     SauceDemoInventory sauceDemoInventory;

    @Given("I am on the inventory page")
    public void iAmOnTheInventoryPage(){
        String url = getDriver().getCurrentUrl();
        assert url != null;

        assertTrue(url.contains(INVENTORY));
    }

    @When("I sort the price from low to high")
    public void iSortThePriceFromLowToHigh() {
        sauceDemoInventory = new SauceDemoInventory(getDriver());
        sauceDemoInventory.filterInventoryItemsBy(PRICE_LOW_TO_HIGH);
    }

    @Then("the first item should be {string} and the price should be {string}")
    public void theFirstItemShouldBeAndThePriceShouldBe(String itemName, String price) {
        sauceDemoInventory = new SauceDemoInventory(getDriver());
        assertTrue(sauceDemoInventory.getFirstItemName().contains(itemName));
        assertEquals(price, sauceDemoInventory.getFirstItemPrice());
    }
}
