package pageObjects.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.AppiumDriver; u can use this instead of Ando
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class pinCodePage {

    public pinCodePage(AndroidDriver<AndroidElement> driver)
    {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(xpath = "//android.widget.Button[@text='1']")
    public WebElement oneButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='2']")
    public WebElement twoButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='3']")
    public WebElement threeButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='4']")
    public WebElement fourButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='5']")
    public WebElement fiveButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='6']")
    public WebElement sixButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='7']")
    public WebElement sevenButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='8']")
    public WebElement eightButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='9']")
    public WebElement nineButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='OK']")
    public WebElement OKButton;

}
