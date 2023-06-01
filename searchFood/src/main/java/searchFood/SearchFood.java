package searchFood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SearchFood {
    public static void main(String[] args) {
        SpringApplication.run(SearchFood.class, args);
    }
}
