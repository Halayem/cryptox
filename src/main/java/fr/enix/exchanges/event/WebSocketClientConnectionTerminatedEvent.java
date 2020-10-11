package fr.enix.exchanges.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WebSocketClientConnectionTerminatedEvent extends ApplicationEvent  {

    private final String message;

    public WebSocketClientConnectionTerminatedEvent(final String message) {
        super(message);
        this.message = message;
    }
}
