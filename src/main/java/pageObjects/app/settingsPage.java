package pageObjects.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;


public class settingsPage {

    private AppiumDriver<MobileElement> driver;

    public settingsPage(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(className = "android.view.View")
    private List<WebElement> settingViewElements;

    @FindBy(xpath = "//android.view.View[@text='Show Phrase']")
    private WebElement showPhrase;

    @FindBy(className = "android.widget.CheckBox")
    private WebElement fingerPrintCheckbox;

    @FindBy(xpath = "//android.view.View[@text='Change pincode']")
    private WebElement changePinCode;

    @FindBy(xpath = "//android.view.View[@text='Advanced settings']")
    private WebElement advancedSettingsDropDown;

    @FindBy(xpath = "//android.view.View[@text='Remove Account From Device']")
    private WebElement removeAccountoption;

    @FindBy(xpath = "//android.widget.Button[@text='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//android.widget.Button[@text='Yes']")
    private WebElement yesButton;

    //popups
    @FindBy(xpath = "//android.view.View[@text='Email has been resent.']")
    private WebElement emailResentText;

    @FindBy(xpath = "//android.widget.Button[@text='Ok']")
    private WebElement OkButton;

    @FindBy(xpath = "//android.view.View[@index='2']")
    private WebElement phraseText;

    @FindBy(xpath = "//android.widget.Button[@text='Close']")
    private WebElement closeButton;

    @FindBy(xpath = "//android.widget.TextView[@text='Scan your fingerprint to authenticate']")
    private WebElement fingerPrintMessage;

    @FindBy(xpath = "//android.widget.Button[@text='CANCEL']")
    private WebElement fingerPrintCancelButton;


    public String get3botUserName(){
        return settingViewElements.get(3).getText();
    }

    private WebElement getEmailSection(){
        return settingViewElements.get(4);
    }

    public String getEmailVerificationStatus(){
        String text = getEmailSection().getText();
        String [] words = text.split("\n");
        String verificationStatus = words[words.length - 1];
        return verificationStatus;
    }

    public void resendVerificationEmail(){
        getEmailSection().click();
    }

    public String getEmail(){
        String text = getEmailSection().getText();
        String [] words = text.split("\n");
        return words[0];
    }

    public Boolean checkEmailResentTextDisplayed(){
        return emailResentText.isDisplayed();
    }

    public void clickOkButton(){
        OkButton.click();
    }

    public pinCodePage clickChangePinCode(){
        changePinCode.click();
        return new pinCodePage(driver);
    }

    public Boolean isChangePinCodeDisplayed(){
        return changePinCode.isDisplayed();
    }

    public void enableFingerPrintCheckbox(){
        fingerPrintCheckbox.click();
    }

    public pinCodePage disableFingerPrintCheckbox(){
        fingerPrintCheckbox.click();
        return new pinCodePage(driver);
    }

    public void clickCancelButton(){
        cancelButton.click();
    }

    public void clickYesButton(){
        yesButton.click();
    }

    public String isFingerPrintBoxChecked(){
        return fingerPrintCheckbox.getAttribute("checked");
    }

    public pinCodePage clickShowPhrase(){
        showPhrase.click();
        return new pinCodePage(driver);
    }

    public Boolean isShowPhraseDisplayed(){
        return showPhrase.isDisplayed();
    }

    public String getPhrase(){
        return phraseText.getText();
    }

    public void clickCloseButton(){
        closeButton.click();
    }

    public Boolean isFingerPrintMessageDisplayed(){
        return fingerPrintMessage.isDisplayed();
    }

    public void clickFingerPrintCancelButton(){
        fingerPrintCancelButton.click();
    }

    public String getVersion(){
        String versionText = settingViewElements.get(8).getText();
        String [] version = versionText.split(" ");
        return version[0];
    }

    public void clickAdvancedSetting(){
        advancedSettingsDropDown.click();
    }

    public void clickRemoveAccount(){
        removeAccountoption.click();
    }

}
