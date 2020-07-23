package fr.enix;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class CryptoxApplication {

    public static void main( String[] args ) {
        SpringApplication.run( CryptoxApplication.class, args );
    }

}
