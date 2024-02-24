package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.spend.SpendRepositoryHibernate;
import guru.qa.niffler.model.SpendJson;

public class DatabaseSpendExtension extends SpendExtension {

    @Override
    SpendJson create(SpendJson spend) {

        CategoryEntity category = new CategoryEntity();
        category.setCategory(spend.category());
        category.setUsername(spend.username());

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setCategory(category);
        spendEntity.setDescription(spend.description());
        spendEntity.setAmount(spend.amount());
        spendEntity.setUsername(spend.username());

        SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();
        SpendEntity created = spendRepositoryHibernate.createSpend(spendEntity);

        return new SpendJson(
                created.getId(),
                created.getSpendDate(),
                created.getCategory().getCategory(),
                created.getCurrency(),
                created.getAmount(),
                created.getDescription(),
                created.getUsername()
        );
    }
}