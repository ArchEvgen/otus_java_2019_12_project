package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// grpcurl --plaintext -d '{"Money":{"Value":1,"Rank":0,"Currency":"USD"},"TargetCurrency":"RUB"}' localhost:9090 ru.otus.grpc.exchange.ExchangeService/Exchange

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
