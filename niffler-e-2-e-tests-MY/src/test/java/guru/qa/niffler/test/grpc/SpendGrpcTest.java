package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SpendGrpcTest extends BaseSpendGrpcTest {

    @DisplayName("Save new spend")
    @Test
    void saveSpendsTest() {
        guru.qa.grpc.niffler.grpc.SpendJson spendJson = guru.qa.grpc.niffler.grpc.SpendJson.newBuilder()
                .setSpendDate(dateToBuilder(new Date()))
                .setCategory("Обучение")
                .setAmount(100)
                .setCurrency(CurrencyValues.RUB)
                .setDescription("Первый курс")
                .setUsername("duck")
                .build();

        guru.qa.grpc.niffler.grpc.SpendJson result = blockingStub.saveSpendForUser(SaveSpendsRequest.newBuilder()
                .setSpend(spendJson)
                .build());

        assertNotEquals("", result.getId());
        assertEquals(100, result.getAmount());
        assertEquals("Обучение", result.getCategory());
        assertEquals("Первый курс", result.getDescription());
        assertEquals("duck", result.getUsername());
        assertEquals(CurrencyValues.RUB, result.getCurrency());
    }

    @DisplayName("Get spends")
    @Test
    void getSpendsTest() {
        GetSpendsRequest getSpendsRequest = GetSpendsRequest.newBuilder()
                .setUsername("duck")
                .setFilterCurrencyValue(1)
                .build();
        GetSpendsResponse spends = blockingStub.getSpendsForUser(getSpendsRequest);
        assertFalse(spends.getSpendJsonList().isEmpty());
        assertTrue(spends.getSpendJsonList().stream()
                .anyMatch(spend -> spend.getAmount() == 100
                        && spend.getDescription().equals("Первый курс")
                        && spend.getCategory().equals("Обучение")));
    }

    @DisplayName("Spend edit")
    @Disabled
    @Test
    void editSpendsTest() {
        guru.qa.grpc.niffler.grpc.SpendJson spendJson = guru.qa.grpc.niffler.grpc.SpendJson.newBuilder()
                .setId("c199bd25-12fa-4d94-9f08-9fc88a693c2f")
                .setSpendDate(dateToBuilder(new Date()))
                .setCategory("Обучение")
                .setAmount(102)
                .setCurrency(CurrencyValues.KZT)
                .setDescription("Второй курс")
                .setUsername("duck")
                .build();

        guru.qa.grpc.niffler.grpc.SpendJson result = blockingStub.editSpendsForUser(SaveSpendsRequest.newBuilder()
                .setSpend(spendJson)
                .build());

        assertNotEquals("", result.getId());
        assertEquals(102, result.getAmount());
        assertEquals("Обучение", result.getCategory());
        assertEquals("Второй курс", result.getDescription());
        assertEquals("duck", result.getUsername());
        assertEquals(CurrencyValues.KZT, result.getCurrency());
    }

    @DisplayName("Delete spend")
    @Test
    void deleteSpendsTest() {
        DeleteSpendsRequest deleteSpendsRequest = DeleteSpendsRequest.newBuilder()
                .setUsername("duck")
                .addIds("8cb00caf-d4bb-47b5-bf0e-5c5e6ddaf9e8")
                .build();
        Empty empty = blockingStub.deleteSpends(deleteSpendsRequest);
        assertEquals(empty.getSerializedSize(),0);
    }

    private com.google.type.Date dateToBuilder(Date date) {
        return com.google.type.Date.newBuilder()
                . setYear(date.getYear())
                .setMonth(date.getMonth())
                .setDay(date.getDate())
                .build();
    }
}
