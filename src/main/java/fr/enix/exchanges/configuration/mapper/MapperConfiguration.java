package fr.enix.exchanges.configuration.mapper;

import fr.enix.exchanges.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public AssetMapper assetMapper() { return new AssetMapper(); }

    @Bean
    public AddOrderMapper addOrderMapper(final AssetMapper assetMapper) { return new AddOrderMapper(assetMapper); }

    @Bean
    public TickerMapper tickerMapper() { return new TickerMapper(); }

    @Bean
    public OpenOrdersMapper openOrdersMapper() { return new OpenOrdersMapper(); }

    @Bean
    public ThresholdMapper thresholdMapper() { return new ThresholdMapper(); }


}
