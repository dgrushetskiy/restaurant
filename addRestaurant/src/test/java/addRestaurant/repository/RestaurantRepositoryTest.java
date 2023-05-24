package addRestaurant.repository;

import addRestaurant.model.Restaurant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private RestaurantRepository restaurantRepository;

    @Test
    void saveRestaurant_Success() {
        // Create a sample restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Sample Restaurant");

        // Mock the DynamoDBMapper save method
        doNothing().when(dynamoDBMapper).save(restaurant);

        // Call the repository method
        Restaurant savedRestaurant = restaurantRepository.saveRestaurant(restaurant);

        // Verify that the save method was called with the correct restaurant
        verify(dynamoDBMapper, times(1)).save(restaurant);

        // Assert that the saved restaurant is the same as the input restaurant
        assertNotNull(savedRestaurant);
        assertEquals(restaurant, savedRestaurant);
    }

    @Test
    void saveRestaurant_ExceptionThrown() {
        // Create a sample restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Sample Restaurant");

        // Mock the DynamoDBMapper save method to throw an exception
        doThrow(RuntimeException.class).when(dynamoDBMapper).save(restaurant);

        // Call the repository method and assert that it throws an exception
        assertThrows(RuntimeException.class, () -> restaurantRepository.saveRestaurant(restaurant));

        // Verify that the save method was called with the correct restaurant
        verify(dynamoDBMapper, times(1)).save(restaurant);
    }

    @Test
    void getRestaurantByName_Success() {
        // Create a sample restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Sample Restaurant");

        // Mock the DynamoDBMapper load method
        when(dynamoDBMapper.load(Restaurant.class, "Sample Restaurant")).thenReturn(restaurant);

        // Call the repository method
        Restaurant retrievedRestaurant = restaurantRepository.getRestaurantByName("Sample Restaurant");

        // Verify that the load method was called with the correct parameters
        verify(dynamoDBMapper, times(1)).load(Restaurant.class, "Sample Restaurant");

        // Assert that the retrieved restaurant is the same as the mocked restaurant
        assertNotNull(retrievedRestaurant);
        assertEquals(restaurant, retrievedRestaurant);
    }

    @Test
    void getRestaurantByName_ExceptionThrown() {
        // Mock the DynamoDBMapper load method to throw an exception
        when(dynamoDBMapper.load(Restaurant.class, "Sample Restaurant")).thenThrow(RuntimeException.class);

        // Call the repository method and assert that it throws an exception
        assertThrows(RuntimeException.class, () -> restaurantRepository.getRestaurantByName("Sample Restaurant"));

        // Verify that the load method was called with the correct parameters
        verify(dynamoDBMapper, times(1)).load(Restaurant.class, "Sample Restaurant");
    }
}
