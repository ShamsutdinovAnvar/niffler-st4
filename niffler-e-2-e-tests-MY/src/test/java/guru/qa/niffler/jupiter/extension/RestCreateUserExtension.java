package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.spend.SpendApiClient;
import guru.qa.niffler.api.userdata.UserDataApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.category.CategoryJson;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.TestData;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.utils.DataUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RestCreateUserExtension extends CreateUserExtension {

    private final AuthApiClient authClient = new AuthApiClient();
    private final UserDataApiClient userApiClient = new UserDataApiClient();
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public UserJson createUser(TestUser user) {
        String username = user.username().isEmpty()
                ? DataUtils.generateRandomUsername()
                : user.username();
        String password = user.password().isEmpty()
                ? "12345"
                : user.password();
        try {
            authClient.register(username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserJson userJson = null;
        try {
            userJson = waitUserAppiar(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserJson(
                userJson.id(),
                userJson.username(),
                userJson.firstname(),
                userJson.surname(),
                CurrencyValues.valueOf(userJson.currency().name()),
                userJson.photo() == null ? "" : new String(userJson.photo()),
                null,
                new TestData(
                        password,
                        null,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
        );
    }

    @Override
    public UserJson createCategory(TestUser user, UserJson createdUser) {

        if (!user.categories().fake()) {
            GenerateCategory categoryData = user.categories();
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryData.category(),
                    createdUser.username()
            );

            try {
                createdUser.testData().categoryJson().add(spendApiClient.addCategory(categoryJson));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createdUser;
    }

    @Override
    public UserJson createSpend(TestUser user, UserJson createdUser) {

        if (!user.spend().fake()) {
            GenerateSpend spendData = user.spend();
            SpendJson spendJson = new SpendJson(
                    UUID.randomUUID(),
                    new Date(),
                    spendData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    createdUser.username()
            );
            try {
                createdUser.testData().spendJson().add(spendApiClient.addSpend(spendJson));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return createdUser;
    }

    private UserJson waitUserAppiar(String username) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (stopwatch.elapsed(TimeUnit.MILLISECONDS) < 20000L) {
            UserJson userJson = userApiClient.getCurrentUser(username);
            if (userJson != null) {
                return userJson;
            } else {
                Thread.sleep(200);
            }
        }
        throw new IllegalStateException("User not found in userdata");
    }
}