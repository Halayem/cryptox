package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision;
import fr.enix.exchanges.model.repository.ApplicationAssetPairTicker;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.service.TradingDecisionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static fr.enix.exchanges.model.business.ApplicationAssetPairTickerTradingDecision.Decision;

@AllArgsConstructor
@Slf4j
public class TradingBearingStrategyDecisionServiceImpl implements TradingDecisionService {


    private final PriceReferenceService priceReferenceService;
    private final ExchangeService exchangeService;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Override
    public Mono<ApplicationAssetPairTickerTradingDecision> getDecision(final ApplicationAssetPairTicker applicationAssetPairTicker) {

        final String applicationAssetPair = applicationAssetPairTicker.getApplicationAssetPair();
        final BigDecimal currentApplicationAssetPairPrice = applicationAssetPairTicker.getPrice();

        return
            Mono.zip(
                priceReferenceService.getPriceReferenceForApplicationAssetPair(applicationAssetPair).map(priceReference -> priceReference.getPrice()),
                applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair)
            )
            .flatMap(objects -> {
                final BigDecimal priceReference = objects.getT1();
                final BigDecimal gap            = objects.getT2();

                return
                    Mono.just(
                    isHighGapReached(currentApplicationAssetPairPrice, priceReference, gap)
                      ? newApplicationAssetPairTickerTradingDecision(Decision.SELL, applicationAssetPairTicker)
                      : isLowGapReached(currentApplicationAssetPairPrice, priceReference, gap)
                        ? newApplicationAssetPairTickerTradingDecision(Decision.BUY, applicationAssetPairTicker)
                        : newApplicationAssetPairTickerTradingDecision(Decision.DO_NOTHING, applicationAssetPairTicker));
            })
            .switchIfEmpty(Mono.just(newApplicationAssetPairTickerTradingDecision(Decision.ERROR, applicationAssetPairTicker)));
    }

    public Mono<BigDecimal> getAmountToSell(final String applicationAssetPair) {
        return
        exchangeService
            .getAvailableAssetForSellPlacement(applicationAssetPair)
            .flatMap(availableAssetForSell -> availableAssetIsLessThanConfiguredAmountToSell(applicationAssetPair, availableAssetForSell)
            .flatMap(isLess ->
                        isLess
                        ? Mono.just(availableAssetForSell)
                        : applicationCurrencyTradingsParameterRepository.getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair))
            );
    }

    private Mono<Boolean> availableAssetIsLessThanConfiguredAmountToSell(final String applicationAssetPair,
                                                                         final BigDecimal availableAssetForSell) {
        return applicationCurrencyTradingsParameterRepository
                .getAmountToSellForBearingStrategyByApplicationAssetPair(applicationAssetPair)
                .map(configuredAmountToSell -> configuredAmountToSell.compareTo(availableAssetForSell) > 0 );
    }


    private boolean isHighGapReached(final BigDecimal lastPrice,
                                     final BigDecimal priceReference,
                                     final BigDecimal gap) {
        return lastPrice.compareTo(priceReference.add(gap)) >= 0;
    }

    private boolean isLowGapReached(final BigDecimal lastPrice,
                                    final BigDecimal priceReference,
                                    final BigDecimal gap) {
        return lastPrice.compareTo(priceReference.subtract(gap)) <= 0;
    }



    private ApplicationAssetPairTickerTradingDecision newApplicationAssetPairTickerTradingDecision(final Decision decision,
                                                                                                   final ApplicationAssetPairTicker applicationAssetPairTicker) {
        return
            ApplicationAssetPairTickerTradingDecision
            .builder()
            .decision                   ( decision )
            .applicationAssetPairTicker ( applicationAssetPairTicker )
            .build();
    }
}
