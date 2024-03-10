package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.spend.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpendCollectionCondition {

    public static CollectionCondition spends(SpendJson... expectedSPends) {
        return new CollectionCondition() {

            private List<String> expectedTexts;

            @Override
            public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
                List<String> actualTexts = lastCheckResult.getActualValue();
                if (actualTexts == null || actualTexts.isEmpty()) {
                    throw new ElementNotFound(collection, toString(), timeoutMs, cause);
                }
                String message = lastCheckResult.getMessageOrElse(() -> "Texts mismatch");
                throw new TextsMismatch(message, collection, expectedTexts, actualTexts, explanation, timeoutMs, cause);
            }

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                expectedTexts = new ArrayList<>();
                if (elements.size() != expectedSPends.length) {
                    return CheckResult.rejected("Incorrect table size", elements);
                }
                boolean checkPassed = true;
                List<String> spendRows = new ArrayList<>();
                for (int i = 0; i < expectedSPends.length; i++) {
                    WebElement row = elements.get(i);
                    SpendJson expectedSpending = expectedSPends[i];
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));

                    spendRows.add("amount: " + cells.get(2).getText() +
                            ", currency: " + cells.get(3).getText() +
                            ", category: " + cells.get(4).getText() +
                            ", description: " + cells.get(5).getText());

                    expectedTexts.add("amount: " + expectedSpending.amount() +
                            ", currency: " + expectedSpending.currency().name() +
                            ", category: " + expectedSpending.category() +
                            ", description: " + expectedSpending.description());

                    if (!Double.valueOf(cells.get(2).getText()).equals(expectedSpending.amount())) {
                        checkPassed = false;
                        break;
                    }
                    if (!cells.get(3).getText().equals(expectedSpending.currency().name())) {
                        checkPassed = false;
                        break;
                    }
                    if (!cells.get(4).getText().equals(expectedSpending.category())) {
                        checkPassed = false;
                        break;
                    }
                    if (!cells.get(5).getText().equals(expectedSpending.description())) {
                        checkPassed = false;
                        break;
                    }
                }

                if (checkPassed) {
                    return CheckResult.accepted();
                } else {
                    return CheckResult.rejected("Incorrect spends content", spendRows);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }
        };

    }
}