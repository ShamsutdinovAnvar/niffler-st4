package guru.qa.niffler.model.currency;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyJson(
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("currencyRate")
        Double currencyRate) {
}
