package pageObjects.web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class loginPage {

    public loginPage(AndroidDriver<AndroidElement> driver)
    {
        PageFactory.initElements(driver, this);
        // Also if you wrapped the driver with AppiumFieldDecorator, it will work
    }

    @FindBy(xpath = "//*[@id=\"topbar-first\"]/div/div[2]/a")
    public WebElement signInButton;

    @FindBy(xpath = "//a[@href='/user/auth/external?authclient=3bot']")
    public WebElement _3botLoginOption;

    @FindBy(xpath = "//div[@class=\"v-text-field__slot\"]/input")
    public WebElement nameField;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/main/div/div/section/div[1]/div/div/form/div[2]/button")
    public WebElement _3botSignInButton;

    @FindBy(xpath = "//a[@id='space-menu']")
    public WebElement mySpacesMenu;

}
