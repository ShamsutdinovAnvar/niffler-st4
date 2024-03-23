package guru.qa.niffler.page;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class HeaderPage extends BasePage<HeaderPage> {

    private final SelenideElement tableContainer = $(".people-content");

    @Step("Перейти на страницу со списком всех пользователей")
    public AllPeoplePage goToAllPeoplePage() {
        $x("//a[contains(@href, 'people')]").click();
        return new AllPeoplePage();
    }
    @Step("Перейти на страницу со списком друзей")
    public FriendsPage goToFriendsPage() {
        $x("//a[contains(@href, 'friends')]").click();
        return new FriendsPage();
    }
    @Step("Check that the page is loaded")
    @Override
    public HeaderPage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }
}