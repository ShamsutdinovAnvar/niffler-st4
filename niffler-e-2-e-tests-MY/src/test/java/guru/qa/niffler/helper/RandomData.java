package guru.qa.niffler.helper;

import com.github.javafaker.Faker;

public class RandomData {
    private static final Faker faker = new Faker();

    public static String
            userName = faker.name().firstName(),
            userPassword = faker.beer().hop();

}
