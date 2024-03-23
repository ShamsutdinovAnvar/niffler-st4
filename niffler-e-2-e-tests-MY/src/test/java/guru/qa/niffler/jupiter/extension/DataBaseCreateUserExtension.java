package guru.qa.niffler.jupiter.extension;


import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.category.CategoryRepository;
import guru.qa.niffler.db.repository.category.CategoryRepositoryHibernate;
import guru.qa.niffler.db.repository.spend.SpendRepository;
import guru.qa.niffler.db.repository.spend.SpendRepositoryHibernate;
import guru.qa.niffler.db.repository.user.UserRepository;
import guru.qa.niffler.db.repository.user.UserRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.category.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.TestData;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.utils.DataUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class DataBaseCreateUserExtension extends CreateUserExtension {

    private static UserRepository userRepository = new UserRepositoryHibernate();
    private SpendRepository spendRepository = new SpendRepositoryHibernate();
    private CategoryRepository categoryRepository = new CategoryRepositoryHibernate();


    @Override
    public UserJson createUser(TestUser user) {
        String username = user.username().isEmpty()
                ? DataUtils.generateRandomUsername()
                : user.username();
        String password = user.password().isEmpty()
                ? "12345"
                : user.password();

        UserAuthEntity userAuth = new UserAuthEntity();
        userAuth.setUsername(username);
        userAuth.setPassword(password);
        userAuth.setEnabled(true);
        userAuth.setAccountNonExpired(true);
        userAuth.setAccountNonLocked(true);
        userAuth.setCredentialsNonExpired(true);
        AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        userAuth.addAuthorities(authorities);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setCurrency(CurrencyValues.RUB);

        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(userEntity);
        return new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getSurname(),
                guru.qa.niffler.model.currency.CurrencyValues.valueOf(userEntity.getCurrency().name()),
                userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
                null,
                new TestData(
                        password,
                        null,
                        new ArrayList<>(),
                        new ArrayList<>()
                )
        );
    }

    @Override
    public UserJson createCategory(TestUser user, UserJson createdUser) {

        GenerateCategory categoryData = user.categories();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(createdUser.username());
        categoryEntity.setCategory(categoryData.category());

        CategoryEntity category = categoryRepository.createCategory(categoryEntity);

        CategoryJson categoryJson = new CategoryJson(
                category.getId(),
                category.getCategory(),
                category.getUsername()
        );

        createdUser.testData().categoryJson().add(categoryJson);
        return createdUser;
    }

    @Override
    public UserJson createSpend(TestUser user, UserJson createdUser) {
        GenerateSpend spendData = user.spend();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(createdUser.username());
        categoryEntity.setCategory(createdUser.testData().categoryJson().get(0).category());

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(createdUser.username());
        spendEntity.setCategory(categoryEntity);
        spendEntity.setCurrency(
                guru.qa.niffler.model.currency.CurrencyValues.RUB);
        spendEntity.setAmount(spendData.amount());
        spendEntity.setDescription(spendData.description());

        SpendEntity spend = spendRepository.createSpend(spendEntity);

        SpendJson spendJson = new SpendJson(
                spend.getId(),
                spend.getSpendDate(),
                spend.getCategory().getCategory(),
                spend.getCurrency(),
                spend.getAmount(),
                spend.getDescription(),
                spend.getUsername()
        );

        createdUser.testData().spendJson().add(spendJson);
        return createdUser;
    }
}