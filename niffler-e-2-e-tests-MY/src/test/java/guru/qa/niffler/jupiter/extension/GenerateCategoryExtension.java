package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Objects;
import java.util.Optional;

public class GenerateCategoryExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace GENERATE_CATEGORY_NAMESPACE
            = ExtensionContext.Namespace.create(GenerateCategoryExtension.class);

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = RETROFIT.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateCategory> categoryAnnotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (categoryAnnotation.isPresent()) {
            GenerateCategory categoryData = categoryAnnotation.get();
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryData.category(),
                    categoryData.username()
            );

            boolean categoryNotExist = Objects.requireNonNull(spendApi.getCategories(categoryData.username())
                            .execute().body()).stream()
                    .noneMatch(category -> categoryData.category().equals(category.category()));

            if (categoryNotExist) {
                categoryJson = spendApi.addCategory(categoryJson).execute().body();
            }

            extensionContext.getStore(GENERATE_CATEGORY_NAMESPACE).put(extensionContext.getUniqueId(), categoryJson);
        }
    }
}
