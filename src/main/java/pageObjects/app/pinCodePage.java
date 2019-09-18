package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class pinCodePage {

    public pinCodePage(AppiumDriver<MobileElement> driver)
    {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.view.View[@text='Login']")
    public WebElement loginText;

    @FindBy(xpath = "//android.widget.Button[@text='1']")
    public WebElement oneButton;

    @FindBy(xpath = "//android.widget.Button[@text='2']")
    public WebElement twoButton;

    @FindBy(xpath = "//android.widget.Button[@text='3']")
    public WebElement threeButton;

    @FindBy(xpath = "//android.widget.Button[@text='4']")
    public WebElement fourButton;

    @FindBy(xpath = "//android.widget.Button[@text='5']")
    public WebElement fiveButton;

    @FindBy(xpath = "//android.widget.Button[@text='6']")
    public WebElement sixButton;

    @FindBy(xpath = "//android.widget.Button[@text='7']")
    public WebElement sevenButton;

    @FindBy(xpath = "//android.widget.Button[@text='8']")
    public WebElement eightButton;

    @FindBy(xpath = "//android.widget.Button[@text='9']")
    public WebElement nineButton;

    @FindBy(xpath = "//android.widget.Button[@text='OK']")
    public WebElement OKButton;

    //changing pin elements
    @FindBy(xpath = "//android.view.View[@text='Enter old pincode']")
    public WebElement enterOldPinText;

    @FindBy(xpath = "//android.view.View[@text='Enter new pincode']")
    public WebElement enterNewPinText;

    @FindBy(xpath = "//android.view.View[@text='Confirm new pincode']")
    public WebElement confirmNewPinText;

    @FindBy(xpath = "//android.view.View[@text='You have successfully changed you pincode']")
    public WebElement pinChangedSuccessfullyText;



}
