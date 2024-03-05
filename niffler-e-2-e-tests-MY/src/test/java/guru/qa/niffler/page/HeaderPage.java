package guru.qa.niffler.page;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class HeaderPage extends BasePage<HeaderPage> {

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
}