package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestUser {
    boolean fake() default false;
    String username() default "";

    String password() default "";
    GenerateCategory categories() default @GenerateCategory(fake = true);

    GenerateSpend spend() default @GenerateSpend(fake = true);

    boolean runnable() default true;
}