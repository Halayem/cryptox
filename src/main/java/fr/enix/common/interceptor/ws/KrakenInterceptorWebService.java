package fr.enix.common.interceptor.ws;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class KrakenInterceptorWebService {

    public ExchangeFilterFunction exchangeFilterFunctionResponseErrorInterceptor() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse ->
            Mono.just(clientResponse)
        );
    }
}
