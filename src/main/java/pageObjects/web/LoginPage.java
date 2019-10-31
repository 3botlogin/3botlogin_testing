package pageObjects.web;

import appmain.Base;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.MobileElement;


public class LoginPage {


    public LoginPage(AppiumDriver<MobileElement> driver)
    {
        PageFactory.initElements(driver, this);
        // Also if you wrapped the driver with AppiumFieldDecorator, it will work
    }

    @FindBy(xpath = "//*[@id=\"topbar-first\"]/div/div[2]/a")
    private WebElement signInButton;

    @FindBy(xpath = "//a[@href='/user/auth/external?authclient=3bot']")
    private WebElement _3botLoginOption;

    @FindBy(xpath = "//div[@class=\"v-text-field__slot\"]/input")
    private WebElement nameField;

    @FindBy(xpath = "//*[@id=\"app\"]/div[4]/main/div/div/section/div[1]/div/div/div/div[2]/div[1]/div/form/div[2]/div/div[1]/div/input")
    private WebElement emailField;

    //sign in and register are the same element .. same xpath
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/main/div/div/section/div[1]/div/div/form/div[2]/button")
    private WebElement _3botSignInButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/main/div/div/section/div[1]/div/div/form/div[2]/button")
    private WebElement _3botRegisterButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div[4]/main/div/div/section/div[1]/div/div/div/div[2]/div[1]/div/form/div[4]/button")
    private WebElement continueButton;

    @FindBy(xpath = "//*[@id=\"app\"]/div[4]/main/div/div/section/div[1]/div/div/div/div[2]/div[2]/div/div[3]/div/div[1]/div/input")
    private WebElement phraseSwitch;


    @FindBy(xpath = "//a[@id='space-menu']")
    private WebElement mySpacesMenu;

    //when validating email
    @FindBy(xpath = "//*[@class='subheading pt-3']")
    public WebElement emailValidatedText;

    public void clickSignInButton(){
        signInButton.click();
    }

    public void clickthreebotLoginOption(){
        _3botLoginOption.click();
    }

    public void enter3botName(String name){
        nameField.sendKeys(name);
    }

    public void click3botSignInButton(){
        Base.waitAndClick(_3botSignInButton);
    }

    public Boolean checkIfMySpaceMenuDisplayed(){
        return mySpacesMenu.isDisplayed();
    }


}
