package homeoffice.PageObject;


import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@DefaultUrl("https://www.saucedemo.com/")
public class SauceDemoHome extends PageObject {

    protected WebDriver driver;

    public static final By USERNAME_TXT = By.id("user-name");
    public static final By PASSWORD_TXT = By.id("password");
    public static final By LOGIN_BTN = By.id("login-button");
    public static final By CREDENTIALS_ERROR_MSG = By.cssSelector("h3[data-test='error']");

    public SauceDemoHome(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        driver.findElement(USERNAME_TXT).sendKeys(username);
        driver.findElement(PASSWORD_TXT).sendKeys(password);

        driver.findElement(LOGIN_BTN).click();
    }

    public String getErrorMessage() {
        return driver.findElement(CREDENTIALS_ERROR_MSG).getText();
    }
}
