package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactOwnText;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement usernameElement = $("figcaption");

    @Step("Проверить, что имя пользователя [{username}]")
    public ProfilePage checkUsername(String username) {
        usernameElement.shouldHave(exactOwnText(username));
        return this;
    }
}