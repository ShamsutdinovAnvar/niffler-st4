package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@ExtendWith(CategoryExtension.class)
public @interface GenerateCategory {
    String category() default "";

    String username() default "";

    boolean fake() default false;
}