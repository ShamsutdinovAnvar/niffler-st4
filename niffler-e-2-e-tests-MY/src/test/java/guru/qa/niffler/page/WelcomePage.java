package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {
    public static final String URL = CFG.frontUrl();
    private final SelenideElement loginBtn = $("a[href*='redirect']"),
            registerBtn = $("a[href*='/register']");

    @Step("Перейти на страницу авторизации")
    public WelcomePage loginBtnClick() {
        loginBtn.click();
        return this;
    }

    @Step("Перейти на страницу регистрации")
    public WelcomePage registerBtnClick() {
        registerBtn.click();
        return this;
    }
}