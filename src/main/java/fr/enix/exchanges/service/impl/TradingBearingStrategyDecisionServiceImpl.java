package fr.enix.exchanges.service.impl;

import fr.enix.exchanges.model.repository.Decision;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.service.MarketOfferService;
import fr.enix.exchanges.service.TradingDecisionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
@Slf4j
public class TradingBearingStrategyDecisionServiceImpl implements TradingDecisionService {


    private final PriceReferenceRepository priceReferenceRepository;
    private final MarketOfferService marketOfferService;
    private final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository;

    @Override
    public Mono<Decision> getDecision(final String applicationAssetPair) {
        return
            Mono.zip(
                marketOfferService.getLastPriceByApplicationAssetPair(applicationAssetPair),
                priceReferenceRepository.getPriceReferenceForApplicationAssetPair(applicationAssetPair).map(priceReference -> priceReference.getPrice()),
                applicationCurrencyTradingsParameterRepository.getGapScaleByApplicationAssetPair(applicationAssetPair)
            )

            .flatMap(objects -> {
                final BigDecimal lastPrice      = objects.getT1();
                final BigDecimal priceReference = objects.getT2();
                final BigDecimal gap            = objects.getT3();

                return
                    Mono.just(
                    isHighGapReached(lastPrice, priceReference, gap)
                      ? Decision.SELL
                      : isLowGapReached(lastPrice, priceReference, gap)
                        ? Decision.BUY
                        : Decision.DO_NOTHING);
            })
            .switchIfEmpty(Mono.just(Decision.DO_NOTHING));
    }

    private boolean isHighGapReached(final BigDecimal lastPrice,
                                     final BigDecimal priceReference,
                                     final BigDecimal gap) {
        return lastPrice.compareTo(priceReference.add(gap)) > 0;
    }

    private boolean isLowGapReached(final BigDecimal lastPrice,
                                    final BigDecimal priceReference,
                                    final BigDecimal gap) {
        return lastPrice.compareTo(priceReference.subtract(gap)) < 0;
    }
}
