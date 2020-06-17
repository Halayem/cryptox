package fr.enix.exchanges.service.impl;


import fr.enix.exchanges.model.business.output.TickerOutput;
import fr.enix.exchanges.service.TickerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TickerServiceImpl implements TickerService {

    @Override
    public void marketOfferUpdateHandler(final TickerOutput tickerOutput) {
      log.info( "*** ticker output: {}", tickerOutput);
    }
}
