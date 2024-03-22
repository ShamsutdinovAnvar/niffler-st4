package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.message.SuccessMsg;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    public static final String PAGE_URL = CFG.frontUrl() + "/profile";
    private final SelenideElement userName = $(".avatar-container figcaption");
    private final SelenideElement avatarInput = $("input[type='file']");
    private final SelenideElement avatar = $(".profile__avatar");
    private final SelenideElement submitBtn = $("button[type='submit']");

    public ProfilePage addAvatar(String imagePath) {
        avatar.click();
        avatarInput.uploadFromClasspath(imagePath);
        submitBtn.click();
        checkMessage(SuccessMsg.PROFILE_MSG);
        return this;
    }
    @Step("Save profile")
    public ProfilePage submitProfile() {
        submitBtn.click();
        return this;
    }
    @Step("Check that page is loaded")
    @Override
    public ProfilePage waitForPageLoaded() {
        userName.should(visible);
        return this;
    }
}