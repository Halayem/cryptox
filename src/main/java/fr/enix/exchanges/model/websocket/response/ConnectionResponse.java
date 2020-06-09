package fr.enix.exchanges.model.websocket.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Setter
public class ConnectionResponse {

    private BigInteger connectionID;
    private String event;
    private String status;
    private String version;
}
