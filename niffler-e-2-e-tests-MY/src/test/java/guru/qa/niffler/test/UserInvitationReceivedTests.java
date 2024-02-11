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

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;


@Epic("WEB тесты")
@Feature("Друзья")
@Story("Пользователь получивший приглашение в друзья")
public class UserInvitationReceivedTests extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(INVITATION_RECEIVED) UserJson user) {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage
                .clickLoginButton();
        loginPage
                .setUsername(user.username())
                .setPassword(user.testData().password())
                .clickSignInButton();
    }

    @Test
    @DisplayName("В таблице со всеми пользователями должен быть потенциальный друг с кнопкой [Submit invitation]")
    void allPeopleTableShouldHaveUserWithSubmitInvitationButton(@User(INVITATION_RECEIVED) UserJson user) {
        mainPage
                .clickAllPeopleButton();
        allPeoplePage
                .checkUserHasSubmitInvitationButton(user.testData().friendUsername());
    }

    @Test
    @DisplayName("В таблице с друзьями должен быть потенциальный друг с кнопкой [Submit invitation]")
    void userShouldHaveSubmitInvitationButtonInFriendsTable(@User(INVITATION_RECEIVED) UserJson user) {
        mainPage
                .clickAllPeopleButton();
        friendsPage
                .checkFriendsTableContainsSubmitInvitationButton(user.testData().friendUsername());
    }
}
