package guru.qa.niffler.page;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> {

    private final SelenideElement friendsTable = $(".abstract-table__buttons"),
            friendsTableUserName = $(".abstract-table tbody");
    @Step("Проверка, что в таблице Friends есть друг: {name} со статусом: {state}")
    public FriendsPage checkFriendsTable(String name, String state) {
        friendsTable.$$("tr")
                .find(text(name));
        friendsTable.shouldHave(text(state));
        return this;
    }

    @Step("Проверка, что в таблице Friends есть пользователь с запросом в друзья")
    public FriendsPage checkUserFriendRequest(String name) {
        friendsTableUserName.$$("tr")
                .find(text(name))
                .$("button[class='button-icon button-icon_type_submit']")
                .shouldBe(visible);
        return this;
    }
}