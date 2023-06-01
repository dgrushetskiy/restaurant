package addRestaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AddRestaurant {
    public static void main(String[] args) {
        SpringApplication.run(AddRestaurant.class, args);
    }
}
