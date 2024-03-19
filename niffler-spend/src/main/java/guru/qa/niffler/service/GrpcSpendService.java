package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.*;
import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

@GrpcService
public class GrpcSpendService extends NifflerSpendServiceGrpc.NifflerSpendServiceImplBase {

    private final SpendRepository spendRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public GrpcSpendService(SpendRepository spendRepository, CategoryRepository categoryRepository) {
        this.spendRepository = spendRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public void getSpendsForUser(GetSpendsRequest request, StreamObserver<GetSpendsResponse> responseObserver) {
        CurrencyValues filterCurrency = CurrencyValues.valueOf(request.getFilterCurrency().name());
        Date dateFrom = request.hasDateFrom()
                ? convertFromGoogleDate(request.getDateFrom())
                : null;
        Date dateTo = request.hasDateTo()
                ? convertFromGoogleDate(request.getDateTo())
                : null;

        List<SpendJson> spendJsonLists = getSpendsEntityForUser(request.getUsername(),
                filterCurrency,
                dateFrom,
                dateTo)
                .map(guru.qa.niffler.model.SpendJson::fromEntity)
                .map(this::spendJsonToMessage)
                .toList();
        responseObserver.onNext(GetSpendsResponse.newBuilder()
                .addAllSpendJson(spendJsonLists)
                .build());
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void saveSpendForUser(SaveSpendsRequest request, StreamObserver<SpendJson> responseObserver) {
        final String username = request.getSpend().getUsername();
        final String category = request.getSpend().getCategory();

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(username);
        spendEntity.setSpendDate(convertFromGoogleDate(request.getSpend().getSpendDate()));
        spendEntity.setCurrency(CurrencyValues.valueOf(request.getSpend().getCurrency().name()));
        spendEntity.setDescription(request.getSpend().getDescription());
        spendEntity.setAmount(request.getSpend().getAmount());

        CategoryEntity categoryEntity = categoryRepository.findAllByUsername(username)
                .stream()
                .filter(c -> c.getCategory().equals(category))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

        spendEntity.setCategory(categoryEntity);
        SpendEntity result = spendRepository.save(spendEntity);
        spendJsonToMessage(guru.qa.niffler.model.SpendJson.fromEntity(result));
        responseObserver.onNext(spendJsonToMessage(guru.qa.niffler.model.SpendJson.fromEntity(result)));
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void editSpendsForUser(SaveSpendsRequest request, StreamObserver<SpendJson> responseObserver) {
        Optional<SpendEntity> spendById = spendRepository.findById(UUID.fromString(request.getSpend().getId()));
        if (spendById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find spend by given id: " + spendById);
        } else {
            final String category = request.getSpend().getCategory();
            CategoryEntity categoryEntity = categoryRepository.findAllByUsername(request.getSpend().getUsername())
                    .stream()
                    .filter(c -> c.getCategory().equals(category))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

            SpendEntity spendEntity = spendById.get();
            spendEntity.setSpendDate(convertFromGoogleDate(request.getSpend().getSpendDate()));
            spendEntity.setCategory(categoryEntity);
            spendEntity.setCurrency(CurrencyValues.valueOf(request.getSpend().getCurrency().name()));
            spendEntity.setDescription(request.getSpend().getDescription());
            spendEntity.setAmount(request.getSpend().getAmount());
            SpendEntity result = spendRepository.save(spendEntity);
            spendJsonToMessage(guru.qa.niffler.model.SpendJson.fromEntity(result));
            responseObserver.onNext(spendJsonToMessage(guru.qa.niffler.model.SpendJson.fromEntity(result)));
            responseObserver.onCompleted();
        }
    }

    @Transactional
    @Override
    public void deleteSpends(DeleteSpendsRequest request, StreamObserver<Empty> responseObserver) {
        spendRepository.deleteByUsernameAndIdIn(request.getUsername(), request.getIdsList().stream().map(UUID::fromString).toList());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Transactional(readOnly = true)
    @Override
    public void getStatistic(StatisticRequest request, StreamObserver<StatisticResponse> responseObserver) {
        super.getStatistic(request, responseObserver);
    }

    private @Nonnull
    Stream<SpendEntity> getSpendsEntityForUser(@Nonnull String username,
                                               @Nullable guru.qa.niffler.model.CurrencyValues filterCurrency,
                                               @Nullable Date dateFrom,
                                               @Nullable Date dateTo) {
        dateTo = dateTo == null
                ? new Date()
                : dateTo;

        List<SpendEntity> spends = dateFrom == null
                ? spendRepository.findAllByUsernameAndSpendDateLessThanEqual(username, dateTo)
                : spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(username, dateFrom, dateTo);

        return spends.stream()
                .filter(se -> filterCurrency == null || se.getCurrency() == filterCurrency);
    }

    private com.google.type.Date dateToBuilder(Date date) {
        return com.google.type.Date.newBuilder().
                setYear(date.getYear())
                .setMonth(date.getMonth())
                .setDay(date.getDate())
                .build();
    }

    public @Nonnull guru.qa.grpc.niffler.grpc.SpendJson spendJsonToMessage(@Nonnull guru.qa.niffler.model.SpendJson spendJson) {
        return guru.qa.grpc.niffler.grpc.SpendJson.newBuilder()
                .setId(spendJson.id().toString())
                .setSpendDate(dateToBuilder(spendJson.spendDate()))
                .setCategory(spendJson.category())
                .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(spendJson.currency().name()))
                .setAmount(spendJson.amount())
                .setDescription(spendJson.description())
                .setUsername(spendJson.username())
                .build();
    }

    static java.util.Date convertFromGoogleDate(com.google.type.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.MONTH, date.getMonth() - 1);
        cal.set(Calendar.DAY_OF_MONTH, date.getDay());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}