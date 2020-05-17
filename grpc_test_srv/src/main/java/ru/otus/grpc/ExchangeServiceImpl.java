package ru.otus.grpc;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.otus.grpc.exchange.ExchangeRequest;
import ru.otus.grpc.exchange.ExchangeResponse;
import ru.otus.grpc.exchange.ExchangeServiceGrpc;
import ru.otus.grpc.money.Currency;

@GrpcService
public class ExchangeServiceImpl extends ExchangeServiceGrpc.ExchangeServiceImplBase {
    private static final BigDecimal USD_TO_RUB = BigDecimal.valueOf(7358, 2);

    @Override
    public void exchange(ExchangeRequest request, StreamObserver<ExchangeResponse> responseObserver) {
        if (!request.hasMoney()) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Money must be not empty")
                    .asRuntimeException());
            return;
        }
        if (request.getMoney().getCurrency() == Currency.RUB && request.getTargetCurrency() == Currency.USD) {
            var rsp = ExchangeResponse.newBuilder();
            var result = ProtoUtils.fromMoney(request.getMoney()).divide(USD_TO_RUB, RoundingMode.HALF_EVEN);
            rsp.setMoney(ProtoUtils.toMoney(result, Currency.USD));
            responseObserver.onNext(rsp.build());
            responseObserver.onCompleted();
            return;
        }
        if (request.getMoney().getCurrency() == Currency.USD && request.getTargetCurrency() == Currency.RUB) {
            var rsp = ExchangeResponse.newBuilder();
            var result = ProtoUtils.fromMoney(request.getMoney()).multiply(USD_TO_RUB);
            rsp.setMoney(ProtoUtils.toMoney(result, Currency.RUB));
            responseObserver.onNext(rsp.build());
            responseObserver.onCompleted();
            return;
        }

        responseObserver.onError(Status.FAILED_PRECONDITION
                .withDescription("Can not exchange that currencies")
                .asRuntimeException());
    }


}
