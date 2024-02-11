package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class SpendResolverExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), GenerateSpend.class)
                .isPresent()
                && AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), GenerateCategory.class)
                .isPresent()
                && parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext,
                                      ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(GenerateSpendExtension.GENERATE_SPEND_NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
