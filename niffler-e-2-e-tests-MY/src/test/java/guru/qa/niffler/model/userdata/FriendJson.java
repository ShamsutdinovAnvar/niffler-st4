package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendJson(
        @JsonProperty("username")
        String username) {
}