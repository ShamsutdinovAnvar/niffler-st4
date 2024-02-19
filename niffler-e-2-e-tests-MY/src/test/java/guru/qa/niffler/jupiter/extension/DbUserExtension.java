package guru.qa.niffler.jupiter.extension;


import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.user.UserRepository;
import guru.qa.niffler.db.repository.user.UserRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
public class DbUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DbUserExtension.class);
    private final UserRepository userRepository = new UserRepositoryJdbc();
    Faker faker = new Faker();
    private static final String USER_AUTH = "userAuth";
    private static final String USER = "user";
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Optional<DbUser> dbUser = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(),
                DbUser.class);
        Map<String, Object> userEntities = new HashMap<>();
        if (dbUser.isPresent()) {
            DbUser dbUserData = dbUser.get();
            String userName = dbUserData.userName();
            String password = dbUserData.password();
            if (userName.isEmpty() && password.isEmpty()) {
                userName = faker.funnyName().name();
                password = faker.internet().password(5, 10);
            }
            UserAuthEntity userAuth = new UserAuthEntity();
            UserEntity user = new UserEntity();
            userAuth.setUsername(userName);
            userAuth.setPassword(password);
            userAuth.setEnabled(true);
            userAuth.setAccountNonExpired(true);
            userAuth.setAccountNonLocked(true);
            userAuth.setCredentialsNonExpired(true);
            userAuth.setAuthorities(Arrays.stream(Authority.values())
                    .map(e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(e);
                        return ae;
                    }).toList()
            );
            user.setUsername(userName);
            user.setCurrency(CurrencyValues.RUB);
            userRepository.createInAuth(userAuth);
            userRepository.createInUserdata(user);
            userEntities.put(USER_AUTH, userAuth);
            userEntities.put(USER, user);
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), userEntities);
    }
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Map<String, Object> userEntities = (Map<String, Object>) extensionContext.getStore(DbUserExtension.NAMESPACE)
                .get(extensionContext.getUniqueId());
        UserAuthEntity userAuth = (UserAuthEntity) userEntities.get(USER_AUTH);
        UserEntity user = (UserEntity) userEntities.get(USER);
        userRepository.deleteInAuthById(userAuth.getId());
        userRepository.deleteInUserdataById(user.getId());
    }
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType().isAssignableFrom(UserAuthEntity.class);
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(USER_AUTH);
    }
}