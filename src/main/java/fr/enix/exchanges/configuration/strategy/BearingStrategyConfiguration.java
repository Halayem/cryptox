package fr.enix.exchanges.configuration.strategy;

import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import fr.enix.exchanges.strategy.bearing.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BearingStrategyConfiguration {

    @Bean
    public TradingBearingStrategyDecisionHelper tradingBearingStrategyDecisionHelper(final PriceReferenceService priceReferenceService) {
        return new TradingBearingStrategyDecisionHelper(priceReferenceService);
    }

    @Bean
    public TradingBearingStrategyDecision highGapTradingBearingStrategyDecisionImpl(final TradingBearingStrategyDecisionHelper tradingBearingStrategyDecisionHelper,
                                                                                    final ExchangeService exchangeService,
                                                                                    final AssetOrderIntervalRepository assetOrderIntervalRepository,
                                                                                    final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository,
                                                                                    final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper) {

        return new HighGapTradingBearingStrategyDecisionImpl(
                        tradingBearingStrategyDecisionHelper,
                        exchangeService,
                        assetOrderIntervalRepository,
                        applicationCurrencyTradingsParameterRepository,
                        applicationAssetPairTickerMapper
        );
    }

    @Bean
    public TradingBearingStrategyDecision lowGapTradingBearingStrategyDecisionImpl(final TradingBearingStrategyDecisionHelper tradingBearingStrategyDecisionHelper,
                                                                                   final ExchangeService exchangeService,
                                                                                   final AssetOrderIntervalRepository assetOrderIntervalRepository,
                                                                                   final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository,
                                                                                   final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper) {

        return new LowGapTradingBearingStrategyDecisionImpl(
                        tradingBearingStrategyDecisionHelper,
                        exchangeService,
                        assetOrderIntervalRepository,
                        applicationCurrencyTradingsParameterRepository,
                        applicationAssetPairTickerMapper
        );
    }

    @Bean
    public TradingBearingStrategyDecision doNothingTradingBearingStrategyDecisionImpl(final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper) {
        return new DoNothingTradingBearingStrategyDecisionImpl(applicationAssetPairTickerMapper);
    }

    @Bean
    public TradingBearingStrategyDecision errorTradingBearingStrategyDecisionImpl(final TradingBearingStrategyDecisionHelper tradingBearingStrategyDecisionHelper) {
        return new ErrorTradingBearingStrategyDecisionImpl(tradingBearingStrategyDecisionHelper);
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
