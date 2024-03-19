package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerSpendServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public class BaseSpendGrpcTest {

    protected static final Config CFG = Config.getInstance();

    protected static Channel channel;
    protected static NifflerSpendServiceGrpc.NifflerSpendServiceBlockingStub blockingStub;
    protected static NifflerSpendServiceGrpc.NifflerSpendServiceStub stub;

    static {
        channel = ManagedChannelBuilder.forAddress(CFG.spendsGrpcHost(), CFG.spendsGrpcPort())
                .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
                .usePlaintext()
                .build();
        blockingStub = NifflerSpendServiceGrpc.newBlockingStub(channel);
        stub = NifflerSpendServiceGrpc.newStub(channel);
    }
}