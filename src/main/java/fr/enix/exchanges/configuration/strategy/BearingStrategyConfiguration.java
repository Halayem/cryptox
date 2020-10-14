package fr.enix.exchanges.configuration.strategy;

import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import fr.enix.exchanges.strategy.bearing.impl.DoNothingTradingBearingStrategyDecisionImpl;
import fr.enix.exchanges.strategy.bearing.impl.ErrorTradingBearingStrategyDecisionImpl;
import fr.enix.exchanges.strategy.bearing.impl.HighGapTradingBearingStrategyDecisionImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BearingStrategyConfiguration {

    @Bean
    public TradingBearingStrategyDecision highGapTradingBearingStrategyDecisionImpl(final PriceReferenceService priceReferenceService,
                                                                                    final ExchangeService exchangeService,
                                                                                    final AssetOrderIntervalRepository assetOrderIntervalRepository,
                                                                                    final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository) {

        return new HighGapTradingBearingStrategyDecisionImpl(
                        priceReferenceService,
                        exchangeService,
                        assetOrderIntervalRepository,
                        applicationCurrencyTradingsParameterRepository
        );
    }

    @Bean
    public TradingBearingStrategyDecision lowGapTradingBearingStrategyDecisionImpl(final PriceReferenceService priceReferenceService,
                                                                                   final ExchangeService exchangeService,
                                                                                   final AssetOrderIntervalRepository assetOrderIntervalRepository,
                                                                                   final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository) {

        return new HighGapTradingBearingStrategyDecisionImpl(
                        priceReferenceService,
                        exchangeService,
                        assetOrderIntervalRepository,
                        applicationCurrencyTradingsParameterRepository
        );
    }

    @Bean
    public TradingBearingStrategyDecision doNothingTradingBearingStrategyDecisionImpl() {
        return new DoNothingTradingBearingStrategyDecisionImpl();
    }

    @Bean
    public TradingBearingStrategyDecision errorTradingBearingStrategyDecisionImpl(final PriceReferenceService priceReferenceService) {
        return new ErrorTradingBearingStrategyDecisionImpl(priceReferenceService);
    }

    @Bean
    public TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactory(final PriceReferenceService priceReferenceService,
                                                                                       final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository,
                                                                                       final TradingBearingStrategyDecision lowGapTradingBearingStrategyDecisionImpl,
                                                                                       final TradingBearingStrategyDecision highGapTradingBearingStrategyDecisionImpl,
                                                                                       final TradingBearingStrategyDecision doNothingTradingBearingStrategyDecisionImpl,
                                                                                       final TradingBearingStrategyDecision errorTradingBearingStrategyDecisionImpl) {
        return new TradingBearingStrategyDecisionFactory(
                        priceReferenceService,
                        applicationCurrencyTradingsParameterRepository,
                        lowGapTradingBearingStrategyDecisionImpl,
                        highGapTradingBearingStrategyDecisionImpl,
                        doNothingTradingBearingStrategyDecisionImpl,
                        errorTradingBearingStrategyDecisionImpl
        );
    }

}
