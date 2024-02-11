package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friendsTable = $(".table tbody");
    private final SelenideElement noFriendsYetElement = $(byText("There are no friends yet!"));

    @Step("Проверить, что в таблице с друзьями у друга [{friendUsername}] есть статус [You are friends]")
    public FriendsPage checkFriendHasStatusYouAreFriends(String friendUsername) {
        getTableRowByFriendName(friendUsername).$(byText("You are friends")).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице с друзьями у друга [{friendUsername}] есть кнопка [Remove friend]")
    public FriendsPage checkFriendHasRemoveFriendButton(String friendUsername) {
        getTableRowByFriendName(friendUsername).$("[data-tooltip-id='remove-friend']").shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице с друзьями у друга [{friendUsername}] есть аватар")
    public FriendsPage checkFriendHasAvatar(String friendUsername) {
        getTableRowByFriendName(friendUsername).$(".people__user-avatar").shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице с друзьями нет друзей")
    public FriendsPage checkFriendsTableHasNoFriends() {
        friendsTable.$$("tr").shouldHave(size(0));
        noFriendsYetElement.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что таблица с друзьями не пуста")
    public FriendsPage checkFriendsTableNotEmpty() {
        friendsTable.$$("tr").shouldHave(sizeGreaterThan(0));
        noFriendsYetElement.shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице с друзьями у потенциального друга [{friendUsername}] есть кнопка [Submit invitation]")
    public FriendsPage checkFriendsTableContainsSubmitInvitationButton(String friendUsername) {
        getTableRowByFriendName(friendUsername).$("[data-tooltip-id='submit-invitation']").shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице с друзьями нет друга с именем [{username}]")
    public FriendsPage checkFriendsTableNotContainsFriend(String username) {
        friendsTable.$$("td").findBy(exactOwnText(username)).shouldNotBe(visible);
        return this;
    }

    private SelenideElement getTableRowByFriendName(String friendUsername) {
        return friendsTable.$$("tr").findBy(text(friendUsername));
    }
}