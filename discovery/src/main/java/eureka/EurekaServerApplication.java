package eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer  // Enables the Eureka server functionality
@SpringBootApplication  // Indicates that this class is a Spring Boot application
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        // Starts the Spring Boot application and initializes the Eureka server
    }
}
