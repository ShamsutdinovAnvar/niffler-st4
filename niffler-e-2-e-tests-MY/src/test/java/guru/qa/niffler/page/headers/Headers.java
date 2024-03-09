package guru.qa.niffler.page.headers;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class Headers {

    private final SelenideElement friendsBtn = $("a[href*='friends']"),
            allPeopleBtn = $("a[href*='people']");

    @Step("Перейти на страницу Friends")
    public Headers clickFriendsBtn() {
        friendsBtn.click();
        return this;
    }

    @Step("Перейти на страницу All People")
    public Headers clickAllPeoplesBtn() {
        allPeopleBtn.click();
        return this;
    }
}