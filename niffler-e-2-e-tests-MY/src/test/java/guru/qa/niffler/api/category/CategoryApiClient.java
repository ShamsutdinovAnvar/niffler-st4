package guru.qa.niffler.api.category;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.category.CategoryJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class CategoryApiClient extends RestClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(Config.getInstance().spendServiceUrl());
        this.categoryApi = retrofit.create(CategoryApi.class);
    }

    @Step("Create category")
    public CategoryJson createCategory(CategoryJson category) throws IOException {
        return categoryApi.addCategory(category)
                .execute()
                .body();
    }

    @Step("Get categories for user '{userName}'")
    public List<CategoryJson> getCategories(String userName) throws IOException {
        return categoryApi.getCategories(userName)
                .execute()
                .body();
    }
}
