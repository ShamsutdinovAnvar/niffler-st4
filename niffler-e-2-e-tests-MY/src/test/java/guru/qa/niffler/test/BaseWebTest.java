package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.*;
import guru.qa.niffler.page.headers.Headers;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

    protected final LoginPage loginPage = new LoginPage();
    protected final Headers headers = new Headers();
    protected final FriendsPage friendsPage = new FriendsPage();
    protected final AllPeoplePage allPeoplePage = new AllPeoplePage();
    protected final MainPage mainPage = new MainPage();
    protected final WelcomePage welcomePage = new WelcomePage();
    protected final RegisterPage registerPage = new RegisterPage();

    static {
        Configuration.browserSize = "1980x1024";
        Configuration.browser = "chrome";
    }
}