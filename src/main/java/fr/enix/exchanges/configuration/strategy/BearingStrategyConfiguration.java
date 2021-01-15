package fr.enix.exchanges.configuration.strategy;

import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.AssetOrderIntervalRepository;
import fr.enix.exchanges.service.ApplicationCurrencyTradingsBearingStrategy;
import fr.enix.exchanges.service.ExchangeService;
import fr.enix.exchanges.service.PriceReferenceService;
import fr.enix.exchanges.strategy.bearing.AmountEnhancerService;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecision;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import fr.enix.exchanges.strategy.bearing.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BearingStrategyConfiguration {

    @Bean
    public AmountEnhancerService amountEnhancerService(final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository) {
        return new AmountEnhancerServiceImpl(applicationCurrencyTradingsParameterRepository);
    }

    @Bean
    public TradingBearingStrategyDecision highGapTradingBearingStrategyDecisionImpl(final PriceReferenceService priceReferenceService,
                                                                                    final ExchangeService exchangeService,
                                                                                    final AmountEnhancerService amountEnhancerService,
                                                                                    final ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy,
                                                                                    final AssetOrderIntervalRepository assetOrderIntervalRepository,
                                                                                    final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper) {
        return new HighGapTradingBearingStrategyDecisionImpl(
                        priceReferenceService,
                        exchangeService,
                        amountEnhancerService,
                        applicationCurrencyTradingsBearingStrategy,
                        assetOrderIntervalRepository,
                        applicationAssetPairTickerMapper
        );
    }

    @Bean
    public TradingBearingStrategyDecision lowGapTradingBearingStrategyDecisionImpl(final PriceReferenceService priceReferenceService,
                                                                                   final ExchangeService exchangeService,
                                                                                   final AmountEnhancerService amountEnhancerService,
                                                                                   final ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy,
                                                                                   final AssetOrderIntervalRepository assetOrderIntervalRepository,
                                                                                   final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper) {

        return new LowGapTradingBearingStrategyDecisionImpl(
                        priceReferenceService,
                        exchangeService,
                        amountEnhancerService,
                        applicationCurrencyTradingsBearingStrategy,
                        assetOrderIntervalRepository,
                        applicationAssetPairTickerMapper
        );
    }

    @Bean
    public TradingBearingStrategyDecision doNothingTradingBearingStrategyDecisionImpl(final ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper) {
        return new DoNothingTradingBearingStrategyDecisionImpl(applicationAssetPairTickerMapper);
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
