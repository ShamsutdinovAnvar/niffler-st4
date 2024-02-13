package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.helper.RandomData;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataBaseCreateUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace DB_CREATE_USER_NAMESPACE
            = ExtensionContext.Namespace.create(DataBaseCreateUserExtension.class);
    private final static UserRepository userRepository = UserRepository.getRepository();
    private final static String userAuthKey = "userAuth";
    private final static String userDataKey = "user";

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<DbUser> dbUserAnnotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class
        );

        if (dbUserAnnotation.isPresent()) {
            DbUser dbUser = dbUserAnnotation.get();
            String username = dbUser.username().isEmpty() ? RandomData.userName : dbUser.username();
            String password = dbUser.password().isEmpty() ? RandomData.userPassword : dbUser.password();

            UserAuthEntity userAuth = new UserAuthEntity();
            userAuth.setUsername(username);
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

            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setCurrency(CurrencyValues.RUB);

            userRepository.createInAuth(userAuth);
            userRepository.createInUserdata(user);

            Map<String, Object> createdUser = new HashMap<>();
            createdUser.put(userAuthKey, userAuth);
            createdUser.put(userDataKey, user);

            extensionContext.getStore(DB_CREATE_USER_NAMESPACE).put(extensionContext.getUniqueId(), createdUser);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Optional<DbUser> dbUserAnnotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DbUser.class
        );

        if (dbUserAnnotation.isPresent()) {
            Map createdUser = extensionContext.getStore(DB_CREATE_USER_NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
            userRepository.deleteInAuthById(((UserAuthEntity) createdUser.get(userAuthKey)).getId());
            userRepository.deleteInUserdataById(((UserEntity) createdUser.get(userDataKey)).getId());
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), DbUser.class)
                .isPresent() &&
                parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(DB_CREATE_USER_NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .get(userAuthKey);
    }
}
