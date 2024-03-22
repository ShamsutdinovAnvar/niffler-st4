package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserRepositoryExtension.class)
public class LoginTest extends BaseWebTest {

    @TestUser()
    @Test
    void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {

        Selenide.open(Config.getInstance().mainPageUrl());
        loginPage.clickNifflerAuthorizationPage()
                .setUserName(userAuth.getUsername())
                .setPass(userAuth.getPassword())
                .submit()
                .checkMainContent();
    }
}