package fr.enix.exchanges.configuration.mapper;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.ApplicationAssetPairTickerMapper;
import fr.enix.exchanges.mapper.PriceReferenceMapper;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public AddOrderMapper addOrderMapper( final CurrenciesRepresentationService currenciesRepresentationService) {
        return new AddOrderMapper(currenciesRepresentationService);
    }

    @Bean
    public ApplicationAssetPairTickerMapper applicationAssetPairTickerMapper() {
        return new ApplicationAssetPairTickerMapper();
    }

    @Bean
    public PriceReferenceMapper priceReferenceMapper() {
        return new PriceReferenceMapper();
    }
}
