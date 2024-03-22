package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TestData(
        @JsonIgnore String password
//        @JsonIgnore UserQueue.UserType userTypeQueue
) {
}
