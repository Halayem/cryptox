package fr.enix.exchanges.configuration.service;

import fr.enix.exchanges.adaptor.ApplicationAssetPairTickerDecisionAdaptor;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.TickerMapper;
import fr.enix.exchanges.repository.ApplicationCurrencyTradingsParameterRepository;
import fr.enix.exchanges.repository.ExchangeRepository;
import fr.enix.exchanges.repository.PriceReferenceRepository;
import fr.enix.exchanges.repository.TickerHistoryRepository;
import fr.enix.exchanges.service.*;
import fr.enix.exchanges.service.impl.*;
import fr.enix.exchanges.strategy.bearing.TradingBearingStrategyDecisionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfiguration {

    @Bean
    public ExchangeService exchangeService(final ExchangeRepository exchangeRepository) {
        return new ExchangeServiceImpl(exchangeRepository);
    }

    @Bean
    public PriceVariationService priceVariationService() {
        return new PriceVariationServiceImpl();
    }

    @Bean
    public MarketOfferService marketOfferService(final MarketPlaceService       marketPlaceService,
                                                 final PriceVariationService    priceVariationService,
                                                 final TickerHistoryRepository  tickerHistoryRepository) {

        return new MarketOfferServiceDBAccessOptimizerImpl(
                    marketPlaceService,
                    priceVariationService,
                    tickerHistoryRepository
        );
    }

    @Bean
    public TradingDecisionService tradingBearingStrategyDecisionServiceImpl(final TradingBearingStrategyDecisionFactory tradingBearingStrategyDecisionFactory) {
        return new TradingBearingStrategyDecisionServiceImpl(tradingBearingStrategyDecisionFactory);
    }

    @Bean
    public PriceReferenceService priceReferenceService(final PriceReferenceRepository priceReferenceRepository) {
        return new PriceReferenceServiceImpl(priceReferenceRepository);
    }

    @Bean
    public TickerService tickerService(final ExchangeService exchangeService,
                                       final TradingDecisionService tradingDecisionService,
                                       final MarketOfferService marketOfferService,
                                       final CurrenciesRepresentationService currenciesRepresentationService,
                                       final ApplicationAssetPairTickerDecisionAdaptor applicationAssetPairTickerDecisionAdaptor,
                                       final TickerMapper tickerMapper,
                                       final AddOrderMapper addOrderMapper) {
        return
            new TickerServiceImpl(
                    exchangeService,
                    tradingDecisionService,
                    marketOfferService,
                    currenciesRepresentationService,
                    applicationAssetPairTickerDecisionAdaptor,
                    tickerMapper,
                    addOrderMapper
            );
    }

    @Bean
    public ApplicationCurrencyTradingsBearingStrategy applicationCurrencyTradingsBearingStrategy(final ApplicationCurrencyTradingsParameterRepository applicationCurrencyTradingsParameterRepository) {
        return
            new ApplicationCurrencyTradingsBearingStrategy(
                new ApplicationCurrencyTradingsDynamicBearingStrategyServiceImpl(),
                new ApplicationCurrencyTradingsStaticBearingStrategyServiceImpl(applicationCurrencyTradingsParameterRepository),
                applicationCurrencyTradingsParameterRepository
            );
    }

}
