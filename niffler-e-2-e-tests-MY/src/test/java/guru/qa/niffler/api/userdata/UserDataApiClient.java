package guru.qa.niffler.api.userdata;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class UserDataApiClient extends RestClient {

    private final UserDataApi userDataApi;

    public UserDataApiClient() {
        super(Config.getInstance().userDataServiceUrl());
        this.userDataApi = retrofit.create(UserDataApi.class);
    }
}