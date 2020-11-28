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
        return exchangeRepository.getAvailableAssetForBuyPlacementByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return exchangeRepository.getAvailableAssetForSellPlacementByApplicationAssetPair(applicationAssetPair);
    }

    @Override
    public Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput) {
        return Mono.defer       ( () -> exchangeRepository.addOrder  (addOrderInput) )
                   .retryWhen   ( retryAddOrderStrategy );
    }

    private final long retryAddOrderMaxAttempts         = 5L;
    private final long retryAddOrderFixedDelayInSeconds = 1L;
    private final Retry retryAddOrderStrategy = Retry
                                                .fixedDelay     (retryAddOrderMaxAttempts, Duration.ofSeconds(retryAddOrderFixedDelayInSeconds))
                                                .filter         (exception -> exception instanceof KrakenEapiInvalidNonceException)
                                                .doAfterRetry   (exception -> log.warn("retry add order, max attempts: {}, fixed delay: {} seconds", retryAddOrderMaxAttempts, retryAddOrderFixedDelayInSeconds));

}
