package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(UsersQueueExtension.class)
public @interface User {

    UserType value();

    enum UserType {
        WITH_FRIENDS,
        INVITATION_SENT,
        INVITATION_RECEIVED
    }
}