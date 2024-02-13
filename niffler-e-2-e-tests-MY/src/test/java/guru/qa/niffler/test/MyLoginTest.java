package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MyLoginTest {

    @DbUser(username = "testUser", password = "12345")
    @Test
    void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userAuth.getUsername());
        $("input[name='password']").setValue(userAuth.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }

    @DbUser
    @Test
    void statisticShouldBeVisibleAfterLoginCreateRandomUser(UserAuthEntity userAuth) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userAuth.getUsername());
        $("input[name='password']").setValue(userAuth.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
