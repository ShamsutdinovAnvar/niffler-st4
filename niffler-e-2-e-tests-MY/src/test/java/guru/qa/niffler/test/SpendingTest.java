package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SpendingTest extends BaseWebTest {
  LoginPage loginPage = new LoginPage();
  MainPage mainPage = new MainPage();
  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    loginPage.clickNifflerAuthorizationPage()
            .setUsername("duck")
            .setPassword("12345")
            .clickSignInButton();
  }

  @GenerateCategory(
          username = "duck",
          category = "Обучение"
  )

  @GenerateSpend(
          username = "duck",
          description = "QA.GURU Advanced 4",
          category = "Обучение",
          amount = 72500.00,
          currency = CurrencyValues.RUB,
          spendDate = "2024-02-22"
  )

  @Test
  void spendingShouldBeDeletedByButtonDeleteSpendingTest(SpendJson spend) {
    mainPage.findAndClickSelectedCategory(spend.description())
            .clickDeleteSelectedButton()
            .checkSpendingsTableRowsHasSize(0);
  }
}