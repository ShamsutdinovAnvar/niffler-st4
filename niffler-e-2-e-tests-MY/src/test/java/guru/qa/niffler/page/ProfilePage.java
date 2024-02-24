package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement editAvatarBtn = $("button.profile__avatar-edit");
    private final SelenideElement uploadAvatar = $(".edit-avatar__input[type=file]");
    private final SelenideElement avatar = $("img.profile__avatar");
    private final SelenideElement firstname = $("[name=firstname]");
    private final SelenideElement surname = $("[name=surname]");
    private final SelenideElement submitBtn = $("button[type=submit]");
    private final SelenideElement category = $("[name=category]");
    private final SelenideElement addCategoryBtn = $(".add-category__input-container button");
    private final ElementsCollection categories = $$(".categories__list .categories__item");



    @Step("Загрузить аватар")
    public ProfilePage uploadAvatarFromClasspath(String classpath) {
        editAvatarBtn.click();
        uploadAvatar.uploadFromClasspath(classpath);
        return this;
    }

    @Step("Указать имя пользователя '{firstname}'")
    public ProfilePage setFirstname(String firstname) {
        this.firstname.setValue(firstname);
        return this;
    }

    @Step("Указать фамилию пользователя '{surname}'")
    public ProfilePage setSurname(String surname) {
        this.surname.setValue(surname);
        return this;
    }

    @Step("Подтвердить ввод пользовательских данных")
    public ProfilePage submitData() {
        submitBtn.scrollTo().click();
        return this;
    }

    @Step("Выбирать категорию '{category}'")
    public ProfilePage setCategory(String category) {
        this.category.setValue(category);
        return this;
    }

    @Step("Новая категория")
    public ProfilePage addCategory() {
        addCategoryBtn.click();
        return this;
    }

    @Step("Убедиться, что указано имя '{firstname}'")
    public ProfilePage checkFirstname(String firstname) {
        this.firstname.shouldHave(value(firstname));
        return this;
    }

    @Step("Убедиться, что указана фамилия '{surname}'")
    public ProfilePage checkSurname(String surname) {
        this.surname.shouldHave(value(surname));
        return this;
    }
}