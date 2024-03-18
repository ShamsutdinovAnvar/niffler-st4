package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.SpendEntity;

import javax.annotation.Nonnull;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public record SpendJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("spendDate")
    Date spendDate,
    @JsonProperty("category")
    String category,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("amount")
    Double amount,
    @JsonProperty("description")
    String description,
    @JsonProperty("username")
    String username) {

  public static @Nonnull SpendJson fromEntity(@Nonnull SpendEntity entity) {
    return new SpendJson(
        entity.getId(),
        entity.getSpendDate(),
        entity.getCategory().getCategory(),
        entity.getCurrency(),
        entity.getAmount(),
        entity.getDescription(),
        entity.getUsername()
    );
  }
    public static @Nonnull SpendJson fromGrpcMessage(@Nonnull guru.qa.grpc.niffler.grpc.SpendJson spendMessage) {
        return new SpendJson(
                UUID.fromString(spendMessage.getId()),
                convertFromGoogleDate(spendMessage.getSpendDate()),
                spendMessage.getCategory(),
                CurrencyValues.valueOf(spendMessage.getCurrency().name()),
                spendMessage.getAmount(),
                spendMessage.getDescription(),
                spendMessage.getUsername()
        );
    }

    static java.util.Date convertFromGoogleDate(com.google.type.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.MONTH, date.getMonth() - 1);
        cal.set(Calendar.DAY_OF_MONTH, date.getDay());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
