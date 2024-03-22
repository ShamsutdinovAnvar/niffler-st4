package guru.qa.niffler.test.web;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.*;
import guru.qa.niffler.page.headers.Headers;

@WebTest
public abstract class BaseWebTest {

    protected final LoginPage loginPage = new LoginPage();
    protected final Headers headers = new Headers();
    protected final FriendsPage friendsPage = new FriendsPage();
    protected final AllPeoplePage allPeoplePage = new AllPeoplePage();
    protected final MainPage mainPage = new MainPage();
    protected static final Config CFG = Config.getInstance();
    protected final WelcomePage welcomePage = new WelcomePage();
    protected final RegisterPage registerPage = new RegisterPage();

    static {
        Configuration.browserSize = "1980x1024";
        Configuration.browser = "chrome";
    }
}