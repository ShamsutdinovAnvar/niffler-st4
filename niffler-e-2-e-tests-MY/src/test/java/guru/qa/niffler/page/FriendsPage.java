package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage extends BasePage<FriendsPage> {

    private final String submitInvitationButton = "//*[@data-tooltip-id = 'submit-invitation']";
    private final String declineInvitationButton = "//*[@data-tooltip-id = 'decline-invitation']";
    public HeaderPage header = new HeaderPage();

    private SelenideElement xPathUserRowFactory(String userName) {
        return $x("//td[normalize-space() = '" + userName + "']/ancestor::tr");
    }

    @Step("Убедиться, что получено приглашение в друзья от пользователя '{fromUser}'")
    public FriendsPage checkThatInvitationReceived(String fromUser) {
        xPathUserRowFactory(fromUser)
                .$x("." + submitInvitationButton)
                .shouldBe(visible);

        xPathUserRowFactory(fromUser)
                .$x("." + declineInvitationButton)
                .shouldBe(visible);

        return this;
    }

    @Step("Убедиться, что на странице отсутствует информация о пользователе '{user}'")
    public FriendsPage checkThatInfoAboutUserNotExist(String user) {
        xPathUserRowFactory(user)
                .shouldNotBe(visible);
        return this;
    }

    @Step("Убедиться, что пользователь '{user}' добавлен в друзья")
    public FriendsPage checkThatUserIsFriend(String user) {
        xPathUserRowFactory(user)
                .shouldHave(text("You are friends"));
        return this;
    }
}