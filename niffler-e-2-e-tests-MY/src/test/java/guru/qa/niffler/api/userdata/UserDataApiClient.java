package guru.qa.niffler.api.userdata;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.userdata.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UserDataApiClient extends RestClient {

    private final UserDataApi userDataApi;

    public UserDataApiClient() {
        super(Config.getInstance().userDataServiceUrl());
        this.userDataApi = retrofit.create(UserDataApi.class);
    }


    @Nullable
    public UserJson getCurrentUser(@Nonnull String username) throws Exception {
        return userDataApi.currentUser(username)
                .execute()
                .body();
    }
}