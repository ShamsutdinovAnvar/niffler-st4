package guru.qa.niffler.api.currency;

import guru.qa.niffler.model.currency.CurrencyCalculateJson;
import guru.qa.niffler.model.currency.CurrencyJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface CurrencyApi {

    @GET("/getAllCurrencies")
    Call<List<CurrencyJson>> getAllCurrencies();

    @POST("/calculate")
    Call<CurrencyCalculateJson> getAllCurrencies(@Body CurrencyCalculateJson currencyCalculate);
}