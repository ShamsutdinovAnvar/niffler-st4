package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.user.UsersQueueExtension;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;
@ExtendWith(UsersQueueExtension.class)
public class UserFriendInvitationSentTest extends BaseWebTest {
    @BeforeEach
    void doLogin(@User(INVITATION_SEND) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        loginPage.clickNifflerAuthorizationPage()
                .setUserName(user.username())
                .setPass(user.testData().password())
                .submit();
    }
    @Test
    @DisplayName("Проверка что у пользователя есть отправленный запрос на добавление в друзья")
    void userSentAnInvitationTest() {
        headers.clickAllPeoplesBtn();
        allPeoplePage.checkAllPeopleTableHasPendingInv("Pending invitation");
    }
    @Test
    @DisplayName("Проверка что в таблице All People отображается пользователь, которого хотим добавить")
    void requestedUserFriendTest(@User(INVITATION_SEND) UserJson user) {
        headers.clickAllPeoplesBtn();
        allPeoplePage.checkAllPeopleHasRequestedUserName(user.testData().friendName());
    }
}
