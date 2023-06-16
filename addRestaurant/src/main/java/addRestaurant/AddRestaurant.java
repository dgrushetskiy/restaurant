package addRestaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient // Enables the application to act as a Eureka client for service registration and discovery
@SpringBootApplication // Indicates that this class is the entry point of the Spring Boot application
public class AddRestaurant {
    public static void main(String[] args) {
        SpringApplication.run(AddRestaurant.class, args);
    }
}
