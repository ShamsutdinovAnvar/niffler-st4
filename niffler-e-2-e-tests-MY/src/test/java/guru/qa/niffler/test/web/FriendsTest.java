package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.CreateUserExtension;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, CreateUserExtension.class, ApiLoginExtension.class})
public class FriendsTest extends BaseWebTest {

    @Test
    @ApiLogin(user = @DbUser())
    void friendsTableShouldNotBeEmpty0() {
        Selenide.open(FriendsPage.URL);
        System.out.println("");
    }
}