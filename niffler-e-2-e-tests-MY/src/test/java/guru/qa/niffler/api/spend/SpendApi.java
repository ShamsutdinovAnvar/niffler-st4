package guru.qa.niffler.api.spend;

import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface SpendApi {

  @POST("/addSpend")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @GET("/spends")
  Call<List<SpendJson>> getUserSpends(@Query("username") String username,
                                      @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                      @Query("from") @Nullable Date from,
                                      @Query("to") @Nullable Date to
  );

  @GET("/statistic")
  Call<List<SpendJson>> getUserStatistic(@Query("username") String username,
                                         @Query("userCurrency") CurrencyValues userCurrency,
                                         @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                         @Query("from") @Nullable Date from,
                                         @Query("to") @Nullable Date to
  );

  @PATCH("/editSpend")
  Call<SpendJson> updateSpend(@Body SpendJson spend);

  @DELETE("/deleteSpends")
  void deleteUserSpends(@Query("username") String username, @Query("ids") List<String> ids);
}