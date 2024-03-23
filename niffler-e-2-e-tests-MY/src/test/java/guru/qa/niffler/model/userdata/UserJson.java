package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.userdata.wsdl.FriendState;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        String photo,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("friendState")
        FriendState friendState,
        @JsonIgnore
        TestData testData
) {
        public UserJson additionTestData(TestData testData) {
                return new UserJson(id,
                        username,
                        firstname,
                        surname,
                        currency,
                        photo,
                        friendState,
                        testData
                );
        }
}