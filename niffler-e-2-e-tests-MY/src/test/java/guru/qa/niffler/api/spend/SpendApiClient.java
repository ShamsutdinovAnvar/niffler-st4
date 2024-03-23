package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.category.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(Config.getInstance().spendServiceUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("Create spend")
    public SpendJson createSpend(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend)
                .execute()
                .body();
    }
    public SpendJson addSpend(SpendJson spend) throws Exception {
        return spendApi.addSpend(spend).execute().body();
    }

    public CategoryJson addCategory(CategoryJson spend) throws Exception {
        return spendApi.addCategory(spend).execute().body();
    }
}
