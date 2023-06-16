package searchFood;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SearchFoodTests {

    @Test
    void contextLoads() {
        // This test ensures that the application context loads successfully
    }

    @Test
    void main_ApplicationStartsSuccessfully() {
        // Arrange & Act
        assertDoesNotThrow(() -> SpringApplication.run(SearchFood.class));

     }
}
