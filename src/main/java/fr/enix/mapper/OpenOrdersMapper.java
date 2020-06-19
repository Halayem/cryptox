package fr.enix.mapper;

import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;

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
                                                 .build         ()
                              )
                              .collect(Collectors.toList());
    }
}
