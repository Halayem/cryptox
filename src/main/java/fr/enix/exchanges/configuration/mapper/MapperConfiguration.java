package fr.enix.exchanges.configuration.mapper;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.mapper.AssetMapper;
import fr.enix.exchanges.mapper.OpenOrdersMapper;
import fr.enix.exchanges.mapper.TickerMapper;
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

}
