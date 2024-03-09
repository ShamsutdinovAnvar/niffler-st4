package guru.qa.niffler.jupiter.extension.user;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static guru.qa.niffler.model.userdata.UserJson.user;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
          = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static final Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
    friendsQueue.add(user("liza", "zxcvb", WITH_FRIENDS, "alina"));

    Queue<UserJson> invitationSent = new ConcurrentLinkedQueue<>();
    invitationSent.add(user("alisa", "zxcvb", INVITATION_SEND, "diana"));

    Queue<UserJson> invitationReceived = new ConcurrentLinkedQueue<>();
    invitationReceived.add(user("anvar", "zxcvb", INVITATION_RECEIVED, "olga"));

    USERS.put(WITH_FRIENDS, friendsQueue);
    USERS.put(INVITATION_SEND, invitationSent);
    USERS.put(INVITATION_RECEIVED, invitationReceived);
  }

  @Override
  public void beforeEach(ExtensionContext context) {

    List<Method> methods = new ArrayList<>();
    methods.add(context.getRequiredTestMethod());
    Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(BeforeEach.class))
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
    context.getStore(NAMESPACE)
            .put(context.getUniqueId(), usersForTest);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {

    Map<User.UserType, UserJson> usersFromTest = (Map<User.UserType, UserJson>) context.getStore(NAMESPACE)
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

    return (UserJson) extensionContext.getStore(NAMESPACE)
            .get(extensionContext.getUniqueId(), Map.class)
            .get(parameterContext.findAnnotation(User.class).get().value());
  }
}