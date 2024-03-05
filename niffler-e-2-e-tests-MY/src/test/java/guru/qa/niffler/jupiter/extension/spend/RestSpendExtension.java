package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.api.category.CategoryApiClient;
import guru.qa.niffler.api.spend.SpendApiClient;
import guru.qa.niffler.model.category.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.io.IOException;

public class RestSpendExtension extends SpendExtension {

    private static final CategoryApiClient CATEGORY_API = new CategoryApiClient();
    private static final SpendApiClient SPEND_API = new SpendApiClient();

    @Override
    SpendJson create(SpendJson spend) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                spend.category(),
                spend.username()
        );

        try {
            CATEGORY_API.createCategory(categoryJson);
            return SPEND_API.createSpend(spend);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}