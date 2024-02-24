package guru.qa.niffler.api.category;

import guru.qa.niffler.model.category.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface CategoryApi {

    @POST("/category")
    Call<CategoryJson> addCategory(@Body CategoryJson spend);

    @GET("/categories")
    Call<List<CategoryJson>> getCategories(@Query("username") String username);
}