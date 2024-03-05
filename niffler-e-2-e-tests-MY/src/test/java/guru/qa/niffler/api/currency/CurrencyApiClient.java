package guru.qa.niffler.api.currency;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class CurrencyApiClient extends RestClient {

    private final CurrencyApi currencyApi;

    public CurrencyApiClient() {
        super(Config.getInstance().currencyServiceUrl());
        this.currencyApi = retrofit.create(CurrencyApi.class);
    }
}