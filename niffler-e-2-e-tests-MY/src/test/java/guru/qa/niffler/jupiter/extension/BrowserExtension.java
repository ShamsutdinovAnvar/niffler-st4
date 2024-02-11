package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;


public class BrowserExtension implements AfterEachCallback, TestExecutionExceptionHandler {

  @Override
  public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Allure.addAttachment(
              "Screenshot on fail",
              new ByteArrayInputStream(
                      Objects.requireNonNull(Selenide.screenshot(OutputType.BYTES))
              )
      );
    }
    throw throwable;
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Selenide.closeWebDriver();
    }
  }
}