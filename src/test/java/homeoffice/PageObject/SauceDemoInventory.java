package homeoffice.PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


public class SauceDemoInventory {

    private final By ADD_SAUCE_LABS_BACKPACK_TO_CART = By.id("add-to-cart-sauce-labs-backpack");
    private final By ADD_SAUCE_LABS_BIKE_LIGHT_TO_CART = By.id("add-to-cart-sauce-labs-bike-light");

    private final By REMOVE_SAUCE_LABS_BACKPACK_FROM_CART = By.id("remove-sauce-labs-backpack");
    private final By REMOVE_SAUCE_LABS_BIKE_LIGHT_FROM_CART = By.id("inventory-item-price");

    private final By INVENTORY_ITEMS_LABEL = By.className("inventory_item_label");
    private final By INVENTORY_ITEMS_PRICE = By.className("inventory_item_price");

    private final By SHOPPING_CART_LINK = By.id("shopping_cart_link");

    private final By INVENTORY_FILTER_DROPDOWN = By.id("product_sort_container");

    protected WebDriver driver;

    public SauceDemoInventory(WebDriver driver) {
        this.driver = driver;
    }

    public void addBackpackToCart() {
        driver.findElement(ADD_SAUCE_LABS_BACKPACK_TO_CART).click();
    }

    public void addBikeLightToCart() {
        driver.findElement(ADD_SAUCE_LABS_BIKE_LIGHT_TO_CART).click();
    }

    public void removeBackpackFromCart() {
        driver.findElement(REMOVE_SAUCE_LABS_BACKPACK_FROM_CART).click();
    }

    public void removeBikeLightFromCart() {
        driver.findElement(REMOVE_SAUCE_LABS_BIKE_LIGHT_FROM_CART).click();
    }

    public void filterInventoryItemsBy(String filter) {
        Select select = new Select(driver.findElement(INVENTORY_FILTER_DROPDOWN));

        switch(filter) {
            case "name a to z":
                select.selectByIndex(0);
                break;
            case "name z to a":
                select.selectByIndex(1);
                break;
            case "price low to high":
                select.selectByIndex(2);
            case "price high to low":
                select.selectByIndex(3);
                break;
        }
    }

    public String getFirstItemName() {
        List<WebElement> inventoryItems = driver.findElements(INVENTORY_ITEMS_LABEL);
        try {
            return inventoryItems.get(0).getText();
        } catch (Exception e) {
            System.out.println("\n\n No Inventory Items Found \n\n");
            throw new RuntimeException(e);
        }
    }

    public String getFirstItemPrice() {
        List<WebElement> inventoryPrices = driver.findElements(INVENTORY_ITEMS_PRICE);
        WebElement firstItem;

        try {
            return inventoryPrices.get(0).getText();
        } catch (Exception e) {
            System.out.println("\n\n No Inventory Items Found \n\n");
            throw new RuntimeException(e);
        }
    }

    public void navigateToCart() {
        driver.findElement(SHOPPING_CART_LINK).click();
    }

}
