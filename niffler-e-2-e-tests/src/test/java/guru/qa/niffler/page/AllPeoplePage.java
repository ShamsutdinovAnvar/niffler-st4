package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {

    private final SelenideElement allPeopleTable = $(".table tbody");

    @Step("Проверить, что в таблице со всеми пользователями есть пользователь [{username}] со статусом [Pending invitation]")
    public AllPeoplePage checkUserHasStatusPendingInvitation(String username) {
        getTableRowByUsername(username).$(byText("Pending invitation")).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице со всеми пользователями есть пользователь [{username}] с аватаром")
    public AllPeoplePage checkUserHasUserAvatar(String username) {
        getTableRowByUsername(username).$(".people__user-avatar").shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в таблице со всеми пользователями у пользователя [{username}] есть кнопка [Submit invitation]")
    public AllPeoplePage checkUserHasSubmitInvitationButton(String username) {
        getTableRowByUsername(username).$("[data-tooltip-id='submit-invitation']").shouldBe(visible);
        return this;
    }

    private SelenideElement getTableRowByUsername(String username) {
        return allPeopleTable.$$("tr").findBy(text(username));
    }
}