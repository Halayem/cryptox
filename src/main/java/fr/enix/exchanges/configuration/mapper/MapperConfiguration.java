package fr.enix.exchanges.configuration.mapper;

import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean public AddOrderMapper addOrderMapper( final CurrenciesRepresentationService currenciesRepresentationService) {
        return new AddOrderMapper(currenciesRepresentationService);
    }

}
