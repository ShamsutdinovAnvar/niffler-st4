package guru.qa.niffler.helper;

import com.github.javafaker.Faker;
import lombok.Getter;

@Getter
public class RandomData {
    private final Faker faker = new Faker();

    public String getUserName(){
        String userName;
        return userName = faker.name().firstName();
    }
    public String getUserPassword(){
        String userPassword;
        return userPassword = faker.beer().hop();
    }
}
