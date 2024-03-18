package guru.qa.niffler.test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTest extends BaseWebTest {

    static {
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("anvar");
        $("input[name='password']").setValue("zxcvb");
        $("button[type='submit']").click();
    }

    @GenerateSpend(
            username = "anvar",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            category = "Обучение",
            currency = CurrencyValues.RUB,
            spendDate = "2024-02-16")
//  @DisabledByIssue("74")
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        mainPage
                .getSpendingTable()
                .checkSpends(spend)
                .selectByText(spend.description());

    Allure.step("Delete spending", () -> $(byText("Delete selected"))
        .click());

    Allure.step("Check that spending was deleted", () -> {
      $(".spendings-table tbody")
          .$$("tr")
          .shouldHave(size(0));
    });
    }
}