package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

    private final SelenideElement allPeopleTableActions = $(".abstract-table__buttons"),
            allPeopleTableUserName = $(".abstract-table tbody");

    @Step("Проверка, что в таблице All People есть отправленный запрос в друзья")
    public AllPeoplePage checkAllPeopleTableHasPendingInv(String text) {
        allPeopleTableActions.shouldHave(text(text));
        return this;
    }

    @Step("Проверка, что в таблице All People отображается имя пользователя, которого хотим добавить")
    public AllPeoplePage checkAllPeopleHasRequestedUserName(String name) {
        allPeopleTableUserName.shouldHave(text(name));
        return this;
    }

    @Step("Проверка, что в таблице Friends есть друг: {user, user2} со статусом: {status, status2}")
    public AllPeoplePage checkFriendsTable(String user, String status) {
        allPeopleTableUserName.$$("tr")
                .find(text(user));
        allPeopleTableUserName.shouldHave(text(status));
        return this;
    }
}