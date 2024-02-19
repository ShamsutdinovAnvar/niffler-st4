package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.model.SpendEntity;

public interface SpendRepository {

    SpendEntity createSpend(SpendEntity spendEntity);
}