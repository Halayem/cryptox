package fr.enix.common.exception.websocket;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KrakenWebSocketConnectionException extends RuntimeException {

    private final String message;

}
