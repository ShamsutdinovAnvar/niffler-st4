package guru.qa.niffler.test;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpendingTest extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        loginPage.clickNifflerAuthorizationPage()
                .setUserName("anvar")
                .setPass("zxcvb")
                .submit();
    }
    @GenerateCategory(
            username = "anvar",
            category = "Обучение"
    )
    @GenerateSpend(
            username = "anvar",
            description = "QA.GURU Advanced 4",
            category = "",
            amount = 72500.00,
            currency = CurrencyValues.RUB,
            spendDate = "2024-02-16")
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpendingTest(SpendJson spend) {
        mainPage.findAndClickSelectedCategory(spend.description())
                .clickDeleteSelectedButton()
                .checkSpendingsTableRowsHasSize(0);
    }
}