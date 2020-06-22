package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.Status;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class OpenOrdersMapper {

    public List<OpenOrderOutput> mapFromOpenOrdersResponseToOpenOrderOutput(final OpenOrdersResponse openOrdersResponse ) {
        return
            openOrdersResponse.getResult()
                              .getOpen  ()
                              .entrySet ()
                              .stream   ()
                              .map      ( entry ->
                                  OpenOrderOutput.builder       ()
                                                 .transactionId (entry.getKey())
                                                 .price         (entry.getValue().getDescr().getPrice())
                                                 .volume        (entry.getValue().getVol())
                                                 .orderType     (AddOrderType.find(entry.getValue().getDescr().getType()))
                                                 .status        (Status.find(entry.getValue().getStatus()))
                                                 .build         ()
                              )
                              .collect(Collectors.toList())
            ;
    }
}
