package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.spend.CategoryApi;
import guru.qa.niffler.api.spend.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestSpendExtension extends SpendExtension {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);
    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    @SneakyThrows
    protected SpendJson create(SpendJson spend) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                spend.category(),
                spend.username()
        );
        categoryApi.addCategory(categoryJson).execute();
        return spendApi.addSpend(spend).execute().body();
    }
}