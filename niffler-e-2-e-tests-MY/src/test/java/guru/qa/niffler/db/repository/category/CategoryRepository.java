package guru.qa.niffler.db.repository.category;

import guru.qa.niffler.db.model.CategoryEntity;

public interface CategoryRepository {

    CategoryEntity createCategory(CategoryEntity category);
}