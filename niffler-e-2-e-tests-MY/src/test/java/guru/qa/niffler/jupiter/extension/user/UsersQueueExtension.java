package guru.qa.niffler.jupiter.extension.user;

import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.userdata.TestData;
import guru.qa.niffler.model.userdata.UserJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.*;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
          = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static Map<UserQueue.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

//  static {
//    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
//    friendsQueue.add(user("liza", "zxcvb", WITH_FRIENDS, "alina"));
//
//    Queue<UserJson> invitationSent = new ConcurrentLinkedQueue<>();
//    invitationSent.add(user("alisa", "zxcvb", INVITATION_SEND, "diana"));
//
//    Queue<UserJson> invitationReceived = new ConcurrentLinkedQueue<>();
//    invitationReceived.add(user("anvar", "zxcvb", INVITATION_RECEIVED, "olga"));
//
//    USERS.put(WITH_FRIENDS, friendsQueue);
//    USERS.put(INVITATION_SEND, invitationSent);
//    USERS.put(INVITATION_RECEIVED, invitationReceived);
//  }

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
    friendsQueue.add(user("dima", "12345", WITH_FRIENDS));
    friendsQueue.add(user("duck", "12345", WITH_FRIENDS));
    commonQueue.add(user("bee", "12345", COMMON));
    commonQueue.add(user("barsik", "12345", COMMON));
    users.put(WITH_FRIENDS, friendsQueue);
    users.put(COMMON, commonQueue);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Parameter[] parameters = context.getRequiredTestMethod().getParameters();

    for (Parameter parameter : parameters) {
      UserQueue annotation = parameter.getAnnotation(UserQueue.class);
      if (annotation != null && parameter.getType().isAssignableFrom(UserJson.class)) {
        UserJson testCandidate = null;
        Queue<UserJson> queue = users.get(annotation.value());
        while (testCandidate == null) {
          testCandidate = queue.poll();
        }
        context.getStore(NAMESPACE).put(context.getUniqueId(), testCandidate);
        break;
      }
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    UserJson userFromTest = context.getStore(NAMESPACE)
            .get(context.getUniqueId(), UserJson.class);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
            .getType()
            .isAssignableFrom(UserJson.class) &&
            parameterContext.getParameter().isAnnotationPresent(UserQueue.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
            .get(extensionContext.getUniqueId(), UserJson.class);
  }

  private static UserJson user(String username, String password, UserQueue.UserType userType) {
    return new UserJson(
            null,
            username,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            new TestData(
                    password,
                    userType,
                    null,
                    null
            )
    );
  }
}