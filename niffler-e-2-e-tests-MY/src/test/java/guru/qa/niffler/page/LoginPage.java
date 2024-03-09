package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    private final WelcomePage welcomePage = new WelcomePage();

    private final SelenideElement
            userNameField = $("input[name='username']"),
            passField = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            mainContent = $(".main-content__section-stats");

    @Step("Перейти на страницу авторизации")
    public LoginPage clickNifflerAuthorizationPage() {
        welcomePage.loginBtnClick();
        return this;
    }

    @Step("Ввести имя пользователя")
    public LoginPage setUserName(String value) {
        userNameField.setValue(value);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage setPass(String value) {
        passField.setValue(value);
        return this;
    }

    @Step("Нажать подтвердить")
    public LoginPage submit() {
        submitButton.click();
        return this;
    }

    @Step("Проверить содержание")
    public LoginPage checkMainContent() {
        mainContent.should(Condition.visible);
        return this;
    }
}