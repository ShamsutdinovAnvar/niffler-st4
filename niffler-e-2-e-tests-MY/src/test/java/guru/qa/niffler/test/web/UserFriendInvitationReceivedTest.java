package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.user.UsersQueueExtension;
import guru.qa.niffler.model.userdata.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.INVITATION_RECEIVED;

@ExtendWith(UsersQueueExtension.class)
public class UserFriendInvitationReceivedTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@UserQueue(INVITATION_RECEIVED) UserJson user) {

        Selenide.open("http://127.0.0.1:3000/main");
        loginPage.clickNifflerAuthorizationPage()
                .setUserName(user.username())
                .setPass(user.testData().password())
                .submit();
    }

    @Test
    @DisplayName("Проверка возможности принять запрос пользователя в друзья")
    void userFriendSubmitInvitationTest(@UserQueue(INVITATION_RECEIVED) UserJson user) {

        headers.clickFriendsBtn();
        friendsPage.checkUserFriendRequest(user.testData().friendName());
    }
}