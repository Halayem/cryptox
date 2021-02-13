package fr.enix.exchanges.service.impl;

import fr.enix.common.exception.eapi.KrakenEapiInvalidNonceException;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.repository.ExchangeRepository;
import fr.enix.exchanges.service.ExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;

@AllArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService  {

    private final ExchangeRepository exchangeRepository;

    @Override
    public Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.defer       ( () -> exchangeRepository.getAvailableAssetForBuyPlacementByApplicationAssetPair( applicationAssetPair ) )
                   .retryWhen   ( retryStrategy );
    }

    @Override
    public Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return Mono.defer       ( () -> exchangeRepository.getAvailableAssetForSellPlacementByApplicationAssetPair( applicationAssetPair ) )
                   .retryWhen   ( retryStrategy );
    }

    @Override
    public Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput) {
        return Mono.defer       ( () -> exchangeRepository.addOrder ( addOrderInput ) )
                   .retryWhen   ( retryStrategy );
    }

    private final long retryStrategyMaxAttempts         = 5L;
    private final long retryStrategyFixedDelayInSeconds = 1L;
    private final Retry retryStrategy =
        Retry.fixedDelay     ( retryStrategyMaxAttempts, Duration.ofSeconds(retryStrategyFixedDelayInSeconds) )
             .filter         ( exception -> exception instanceof KrakenEapiInvalidNonceException)
             .doAfterRetry   ( exception -> log.warn(
                                                 "retry strategy due to {}, max attempts: {}, fixed delay: {} seconds",
                                                 KrakenEapiInvalidNonceException.class.getName(),
                                                 retryStrategyMaxAttempts,
                                                 retryStrategyFixedDelayInSeconds
                                            )
             );

}
