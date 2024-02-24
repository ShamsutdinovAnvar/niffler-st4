package guru.qa.niffler.jupiter.extension.emf;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.jupiter.extension.SuiteExtension;
import jakarta.persistence.EntityManagerFactory;

public class EmfExtension implements SuiteExtension {

    @Override
    public void afterSuite() {
        EmfProvider.INSTANCE.storedEmf().forEach(
                EntityManagerFactory::close
        );
    }
}