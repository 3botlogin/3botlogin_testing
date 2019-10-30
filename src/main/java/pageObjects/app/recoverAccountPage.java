package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static java.time.Duration.ofSeconds;


public class recoverAccountPage {

    private AppiumDriver<MobileElement> driver;

    public recoverAccountPage(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.widget.EditText[@text='Doublename']")
    private WebElement doubleNameField;

    @FindBy(xpath = "//android.widget.EditText[@index='2']")
    private WebElement emailField;

    @FindBy(xpath = "//android.widget.EditText[@index='3']")
    private WebElement phraseField;

    @FindBy(xpath = "//android.widget.Button[@text='Recover Account']")
    private WebElement recoverAccountButton;

    @FindBy(xpath = "//android.widget.Button[@text='PASTE']")
    private WebElement pasteButton;

    @FindBy(xpath = "//android.view.View[@text='Enter Valid Email']")
    private WebElement emailFieldErrorMessage;

    @FindBy(xpath = "//android.view.View[@text='Enter your Seedphrase']")
    private WebElement phraseFieldErrorMessage;

    @FindBy(xpath = "//android.view.View[@index='4']")
    private WebElement generalErrorMessage;


    public void click3botNameField(){
        doubleNameField.click();
    }
    public void enter3botName(String name){
        click3botNameField();
        Actions a = new Actions(driver);
        a.sendKeys(name);
        a.perform();
    }

    public void clickEmailField(){
        emailField.click();
    }
    public void enterEmail(String email){
        clickEmailField();
        Actions a = new Actions(driver);
        a.sendKeys(email);
        a.perform();
    }

    public Boolean isEmailFieldErrorMessageDisplayed(){
        return emailFieldErrorMessage.isDisplayed();
    }

    public void clickBasteButton(){
        pasteButton.click();
    }

    public Boolean isBasteButtonDisplayed(){
        return pasteButton.isDisplayed();
    }

    public void copyPastePhrase(String phrase){
        ((AndroidDriver) driver).setClipboardText(phrase);
        TouchAction t = new TouchAction(driver);
        t.longPress(longPressOptions().withElement(element(phraseField)).
                    withDuration(ofSeconds(2))).release().perform();
        Assert.assertTrue(isBasteButtonDisplayed());
        clickBasteButton();
    }

    public Boolean isPhraseFieldErrorMessage(){
        return phraseFieldErrorMessage.isDisplayed();
    }

    public pinCodePage clickRecoverAccountButton(){
        recoverAccountButton.click();
        return new pinCodePage(driver);
    }

    public String getGeneralErrorMessage(){
        return generalErrorMessage.getText();
    }

}
