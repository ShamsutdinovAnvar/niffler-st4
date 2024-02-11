package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static guru.qa.niffler.model.UserJson.defaultUser;

@SuppressWarnings("unchecked")
public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace USERS_QUEUE_NAMESPACE
          = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    friendsQueue.add(defaultUser("dima", new TestData("12345", "duck")));
    friendsQueue.add(defaultUser("duck", new TestData("12345", "dima")));

    Queue<UserJson> invitationSentQueue = new ConcurrentLinkedQueue<>();
    invitationSentQueue.add(defaultUser("bee", new TestData("12345", "barsik")));
    invitationSentQueue.add(defaultUser("dog", new TestData("12345", "fish")));

    Queue<UserJson> invitationReceivedQueue = new ConcurrentLinkedQueue<>();
    invitationReceivedQueue.add(defaultUser("barsik", new TestData("12345", "bee")));
    invitationReceivedQueue.add(defaultUser("fish", new TestData("12345", "dog")));

    USERS.put(WITH_FRIENDS, friendsQueue);
    USERS.put(INVITATION_SENT, invitationSentQueue);
    USERS.put(INVITATION_RECEIVED, invitationReceivedQueue);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    List<Method> methods = new ArrayList<>();
    methods.add(context.getRequiredTestMethod());
    Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(BeforeEach.class))
            .forEach(methods::add);

    List<Parameter> parameters = methods.stream()
            .map(Executable::getParameters)
            .flatMap(Arrays::stream)
            .filter(parameter -> parameter.isAnnotationPresent(User.class))
            .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
            .toList();

    Map<User.UserType, UserJson> usersForTest = new HashMap<>();

    for (Parameter parameter : parameters) {
      User.UserType userType = parameter.getAnnotation(User.class).value();

      if (usersForTest.containsKey(userType)) {
        continue;
      }

      UserJson testCandidate = null;
      Queue<UserJson> queue = USERS.get(userType);
      while (testCandidate == null) {
        testCandidate = queue.poll();
      }

      usersForTest.put(userType, testCandidate);
    }

    context.getStore(USERS_QUEUE_NAMESPACE)
            .put(context.getUniqueId(), usersForTest);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Map<User.UserType, UserJson> usersFromTest = (Map<User.UserType, UserJson>) context.getStore(USERS_QUEUE_NAMESPACE)
            .get(context.getUniqueId(), Map.class);
    for (User.UserType userType : usersFromTest.keySet()) {
      USERS.get(userType).add(usersFromTest.get(userType));
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
            .getType()
            .isAssignableFrom(UserJson.class) &&
            parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    //noinspection OptionalGetWithoutIsPresent
    return (UserJson) extensionContext.getStore(USERS_QUEUE_NAMESPACE)
            .get(extensionContext.getUniqueId(), Map.class)
            .get(parameterContext.findAnnotation(User.class).get().value());
  }
}