package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;
import java.util.Optional;

public class GenerateSpendExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace GENERATE_SPEND_NAMESPACE
            = ExtensionContext.Namespace.create(GenerateSpendExtension.class);

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = RETROFIT.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateSpend> spendAnnotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        );

        Optional<GenerateCategory> categoryAnnotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (spendAnnotation.isPresent() && categoryAnnotation.isPresent()) {
            GenerateSpend spendData = spendAnnotation.get();
            CategoryJson categoryData = extensionContext.getStore(GenerateCategoryExtension.GENERATE_CATEGORY_NAMESPACE)
                    .get(extensionContext.getUniqueId(), CategoryJson.class);

            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    categoryData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    categoryData.username()
            );

            SpendJson created = spendApi.addSpend(spendJson).execute().body();
            extensionContext.getStore(GENERATE_SPEND_NAMESPACE).put(extensionContext.getUniqueId(), created);
        }
    }
}