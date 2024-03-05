package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {

    private final SelenideElement loginButton = $("a[href*='redirect']");
    private final SelenideElement registerButton = $("a[href*='auth']");

    @Step("Перейти к странице логина")
    public LoginPage goToLoginPage() {
        loginButton.click();
        return new LoginPage();
    }

    @Step("Перейти к странице регистрации")
    public RegisterPage goToRegisterPage() {
        registerButton.click();
        return new RegisterPage();
    }
}
