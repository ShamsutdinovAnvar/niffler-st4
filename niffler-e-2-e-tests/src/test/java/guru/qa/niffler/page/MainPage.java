package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection spendingsTableRows = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deleteSelectedButton = $(byText("Delete selected"));
    private final SelenideElement friendsButton = $("[data-tooltip-id=friends]");
    private final SelenideElement allPeopleButton = $("[data-tooltip-id=people]");
    private final SelenideElement profileButton = $("[data-tooltip-id=profile]");

    @Step("Выбрать трату с описанием [{description}]")
    public MainPage selectSpendingByDescription(String description) {
        spendingsTableRows
                .findBy(text(description))
                .$("td")
                .scrollIntoView(true)
                .click();
        return this;
    }

    @Step("Нажать кнопку [Delete selected]")
    public MainPage clickDeleteSelectedButton() {
        deleteSelectedButton.click();
        return this;
    }

    @Step("Проверить, что в таблице с тратами количество строк равно [{size}]")
    public MainPage checkSpendingsTableRowsHasSize(int size) {
        spendingsTableRows.shouldHave(size(size));
        return this;
    }

    @Step("Нажать кнопку [Friends]")
    public void clickFriendsButton() {
        friendsButton.click();
    }

    @Step("Нажать кнопку [All people]")
    public void clickAllPeopleButton() {
        allPeopleButton.click();
    }

    @Step("Нажать кнопку [Profile]")
    public void clickProfileButton() {
        profileButton.click();
    }
}