package guru.qa.niffler.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.db.model.CategoryEntity;

import javax.annotation.Nonnull;
import java.util.UUID;

public record CategoryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("category")
        String category,
        @JsonProperty("username")
        String username) {
        public static @Nonnull CategoryJson fromEntity(@Nonnull CategoryEntity entity) {
                return new CategoryJson(
                        entity.getId(),
                        entity.getCategory(),
                        entity.getUsername()
                );
        }
}
