package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class PinCodePage {

    private AppiumDriver<MobileElement> driver;

    public PinCodePage(AppiumDriver<MobileElement> driver)
    {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//android.view.View[@text='Login']")
    private WebElement loginText;

    @FindBy(xpath = "//android.widget.Button[@text='OK']")
    private WebElement OKButton;


    //changing pin elements
    @FindBy(xpath = "//android.view.View[@text='Enter old pincode']")
    private WebElement enterOldPinText;

    @FindBy(xpath = "//android.view.View[@text='Enter new pincode']")
    private WebElement enterNewPinText;

    @FindBy(xpath = "//android.view.View[@text='Confirm new pincode']")
    private WebElement confirmNewPinText;

    @FindBy(xpath = "//android.view.View[@text='You have successfully changed you pincode']")
    private WebElement pinChangedSuccessfullyText;


    public void clickNumber(String num){
        driver.findElement(By.xpath("//android.widget.Button[@text=" + num + "]")).click();
    }


    public ResourceAccessPage clickOkButton(){
        OKButton.click();
        return new ResourceAccessPage(driver);

    }

    public String getLoginText(){
        return loginText.getText();
    }

    public Boolean checkEnterOldPinTextDisplayed(){
        return enterOldPinText.isDisplayed();
    }

    public Boolean checkEnterNewPinTextDisplayed(){
        return enterNewPinText.isDisplayed();
    }

    public Boolean checkConfirmNewPinTextDisplayed(){
        return confirmNewPinText.isDisplayed();
    }

    public Boolean checkPinChangedSuccessfullyTextDisplayed(){
        return confirmNewPinText.isDisplayed();
    }

    public ResourceAccessPage enterRightPinCode(){

        clickNumber("1");
        clickNumber("2");
        clickNumber("3");
        clickNumber("4");
        return clickOkButton();
    }

    public void confirmRightPin(){

        enterRightPinCode();
    }
}
