package io.irn.geoloc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application class.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = io.irn.geoloc.events.infrastructure.persistence.EventJpaRepository.class)
@ComponentScan(basePackages = "io.irn.geoloc")
public class ZoocodeEventsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZoocodeEventsApplication.class, args);
    }
}
