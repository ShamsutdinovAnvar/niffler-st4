package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.model.category.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.List;

public record TestData(
        @JsonIgnore String password,
        @JsonIgnore UserQueue.UserType userTypeQueue,
        @JsonIgnore List<CategoryJson> categoryJson,
        @JsonIgnore List<SpendJson> spendJson
) {
}
