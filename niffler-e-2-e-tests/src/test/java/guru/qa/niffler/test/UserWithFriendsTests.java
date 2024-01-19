package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@Epic("WEB тесты")
@Feature("Друзья")
@Story("Пользователь с подтвержденным другом")
public class UserWithFriendsTests extends BaseWebTest {

    @BeforeEach
    void doLoginAndOpenFriendsPage(@User(WITH_FRIENDS) UserJson user) {
        Selenide.open("http://127.0.0.1:3000");
        welcomePage
                .clickLoginButton();
        loginPage
                .setUsername(user.username())
                .setPassword(user.testData().password())
                .clickSignInButton();
        mainPage
                .clickFriendsButton();
    }

    @Test
    @DisplayName("Таблица с друзьями не должна быть пустой")
    void friendsTableShouldNotBeEmpty() {
        friendsPage.checkFriendsTableNotEmpty();
    }

    @Test
    @DisplayName("Должен быть друг со статусом [You are friends]")
    void friendShouldHaveStatusYouAreFriends(@User(WITH_FRIENDS) UserJson user) {
        friendsPage.checkFriendHasStatusYouAreFriends(user.testData().friendUsername());
    }

    @Test
    @DisplayName("У друга должна быть кнопка [Remove friend]")
    void friendShouldHaveRemoveFriendButton(@User(WITH_FRIENDS) UserJson user) {
        friendsPage.checkFriendHasRemoveFriendButton(user.testData().friendUsername());
    }

    @Test
    @DisplayName("У друга должен быть аватар")
    void friendShouldHaveAvatar(@User(WITH_FRIENDS) UserJson user) {
        friendsPage.checkFriendHasAvatar(user.testData().friendUsername());
    }


    @Test
    @DisplayName("Пользователь не должен видеть в таблице с друзьями не связанного пользователя")
    void friendsTableShouldNotHaveNotFriendUser(@User(WITH_FRIENDS) UserJson userWithFriends,
                                                @User(INVITATION_SENT) UserJson userInvitationSent) {
        Assertions.assertNotEquals(null, userInvitationSent);
        Assertions.assertNotEquals(null, userWithFriends);
        Assertions.assertNotEquals(userWithFriends.username(), userInvitationSent.username());
        friendsPage.checkFriendsTableNotContainsFriend(userInvitationSent.username());
    }
}