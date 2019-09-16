package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class recoverAccountPage {

    public recoverAccountPage(AppiumDriver<MobileElement> driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.widget.EditText[@text='Doublename']")
    public WebElement doubleNameField;

    @FindBy(xpath = "//android.widget.EditText[@index='2']")
    public WebElement emailField;

    @FindBy(xpath = "//android.widget.EditText[@index='3']")
    public WebElement phraseField;

    @FindBy(xpath = "//android.widget.Button[@text='Recover Account']")
    public WebElement recoverAccountButton;

    @FindBy(xpath = "//android.widget.Button[@text='PASTE']")
    public WebElement pasteButton;

    @FindBy(xpath = "//android.view.View[@text='Enter Valid Email']")
    public WebElement emailFieldErrorMessage;

    @FindBy(xpath = "//android.view.View[@text='Enter your Seedphrase']")
    public WebElement phraseFieldErrorMessage;

    @FindBy(xpath = "//android.view.View[@index='4']")
    public WebElement generalErrorMessage;

}
