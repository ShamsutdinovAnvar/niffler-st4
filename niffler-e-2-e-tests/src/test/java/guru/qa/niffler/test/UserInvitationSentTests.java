package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

@Epic("WEB тесты")
@Feature("Друзья")
@Story("Пользователь отправивший приглашение в друзья")
public class UserInvitationSentTests extends BaseWebTest {

    @BeforeEach
    void openWelcomePage() {
        Selenide.open("http://127.0.0.1:3000");
    }

    @Test
    @DisplayName("В таблице со всеми пользователями должен быть потенциальный друг со статусом [Pending invitation]")
    void allPeopleTableShouldHaveUserWithStatusPendingInvitation(@User(INVITATION_SENT) UserJson user) {
        welcomePage
                .clickLoginButton();
        loginPage
                .setUsername(user.username())
                .setPassword(user.testData().password())
                .clickSignInButton();
        mainPage
                .clickAllPeopleButton();
        allPeoplePage
                .checkUserHasStatusPendingInvitation(user.testData().friendUsername());
    }

    @Test
    @DisplayName("В таблице со всеми пользователями у потенциального друга должен быть аватар")
    void allPeopleTableShouldHaveUserWithAvatar(@User(INVITATION_SENT) UserJson user) {
        welcomePage
                .clickLoginButton();
        loginPage
                .setUsername(user.username())
                .setPassword(user.testData().password())
                .clickSignInButton();
        mainPage
                .clickAllPeopleButton();
        allPeoplePage
                .checkUserHasUserAvatar(user.testData().friendUsername());
    }

    @Test
    @DisplayName("В таблице с друзьями не должно быть друзей")
    void userShouldNotHaveFriendsInFriendsTable(@User(INVITATION_SENT) UserJson user) {
        welcomePage
                .clickLoginButton();
        loginPage
                .setUsername(user.username())
                .setPassword(user.testData().password())
                .clickSignInButton();
        mainPage
                .clickFriendsButton();
        friendsPage
                .checkFriendsTableHasNoFriends();
    }
}
