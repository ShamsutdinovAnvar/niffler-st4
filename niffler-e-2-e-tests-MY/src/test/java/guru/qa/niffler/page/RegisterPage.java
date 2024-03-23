package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement confirmPasswordInput = $("#passwordSubmit");
    private final SelenideElement submitBtn = $("button.form__submit");
    private final SelenideElement tableContainer = $(".people-content");

    @Step("Указать имя пользователя '{username}'")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Указать пароль '{password}'")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Подтвердить пароль '{confirmPassword}'")
    public RegisterPage setConfirmPassword(String confirmPassword) {
        confirmPasswordInput.setValue(confirmPassword);
        return this;
    }

    @Step("Подтвердить регистрацию пользователя")
    public MainPage submit() {
        submitBtn.click();
        return new MainPage();
    }
    @Step("Check that the page is loaded")
    @Override
    public RegisterPage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }
}