package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import guru.qa.niffler.jupiter.annotation.User;

public record TestData(
        @JsonIgnore String password,
        @JsonIgnore String friendName,
        @JsonIgnore User.UserType userType
) {
}
