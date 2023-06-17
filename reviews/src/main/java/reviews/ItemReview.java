package reviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient //enables the application to act as a Eureka client, allowing it to register with a Eureka server
@SpringBootApplication //Annotation that combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
public class ItemReview {
    public static void main(String[] args) {
        SpringApplication.run(ItemReview.class, args);
    }
}
