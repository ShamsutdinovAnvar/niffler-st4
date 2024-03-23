package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.spend.RestSpendExtension;
import guru.qa.niffler.model.currency.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({RestSpendExtension.class})
public @interface GenerateSpend {

    String username() default "";

    String description() default "";

    String category() default "";

    double amount() default 0;

    CurrencyValues currency() default CurrencyValues.RUB;

    boolean fake() default false;
    String spendDate() default "";
}