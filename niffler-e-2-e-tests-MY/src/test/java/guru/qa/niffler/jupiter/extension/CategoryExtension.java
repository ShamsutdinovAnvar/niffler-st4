package guru.qa.niffler.jupiter.extension;


import guru.qa.niffler.api.spend.CategoryApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.util.Optional;
public class CategoryExtension implements BeforeEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryExtension.class);
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final CategoryApi categoryApi = RETROFIT.create(CategoryApi.class);
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class);
        if (category.isPresent()) {
            GenerateCategory generateCategory = category.get();
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    generateCategory.username(),
                    generateCategory.category()
            );
            categoryApi.addCategory(categoryJson).execute();
            extensionContext.getStore(NAMESPACE)
                    .put("category", categoryJson);
        }
    }
}