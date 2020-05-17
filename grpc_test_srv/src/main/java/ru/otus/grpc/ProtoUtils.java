package ru.otus.grpc;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.otus.grpc.money.Currency;
import ru.otus.grpc.money.Money;

public class ProtoUtils {
    private ProtoUtils() {}

    public static BigDecimal fromMoney(Money money) {
        return BigDecimal.valueOf(money.getValue(), money.getRank());
    }

    public static Money toMoney(BigDecimal value, Currency currency) {
        value = value.setScale(2, RoundingMode.HALF_EVEN);
        return Money.newBuilder()
                .setValue(value.unscaledValue().intValue())
                .setRank(value.scale())
                .setCurrency(currency)
                .build();
    }
}
