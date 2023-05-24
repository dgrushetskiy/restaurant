package updatePrice.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import updatePrice.model.Menu;
import updatePrice.model.MenuList;
import updatePrice.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestaurantRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantRepository = new RestaurantRepository();
        restaurantRepository.setDynamoDBMapper(dynamoDBMapper);
    }

    @Test
    void testSaveRestaurant() {
        // Create a Restaurant object
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Test Restaurant");

        restaurant.setAddress("123 Main St");

        Menu menu1 = new Menu();
        menu1.setItemName("Item 1");
        menu1.setRatings("9.5");
        menu1.setPrice("150.0");

        Menu menu2 = new Menu();
        menu2.setItemName("Item 2");
        menu2.setRatings("8.0");
        menu2.setPrice("120.0");

        List<Menu> menuItems = new ArrayList<>();
        menuItems.add(menu1);
        menuItems.add(menu2);

        MenuList menuList = new MenuList();
        menuList.setItems(menuItems);

        restaurant.setMenuList(menuList);

        // Perform the save operation
        Restaurant savedRestaurant = restaurantRepository.saveRestaurant(restaurant);

        // Verify that the save method of DynamoDBMapper is called once
        verify(dynamoDBMapper, times(1)).save(restaurant);

        // Verify that the returned restaurant is the same as the input restaurant
        assertEquals(restaurant, savedRestaurant);
    }

    @Test
    void testGetRestaurantByRestaurantName() {
        // Create a mock Restaurant object
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantName("Test Restaurant");

        mockRestaurant.setAddress("123 Main St");

        Menu menu1 = new Menu();
        menu1.setItemName("Item 1");
        menu1.setRatings("9.5");
        menu1.setPrice("150.0");

        Menu menu2 = new Menu();
        menu2.setItemName("Item 2");
        menu2.setRatings("8.0");
        menu2.setPrice("120.0");

        List<Menu> menuItems = new ArrayList<>();
        menuItems.add(menu1);
        menuItems.add(menu2);

        MenuList menuList = new MenuList();
        menuList.setItems(menuItems);

        mockRestaurant.setMenuList(menuList);

        // Specify the behavior of DynamoDBMapper's load method
        when(dynamoDBMapper.load(Restaurant.class, "Test Restaurant")).thenReturn(mockRestaurant);

        // Perform the get operation
        Restaurant retrievedRestaurant = restaurantRepository.getRestaurantByRestaurantName("Test Restaurant");

        // Verify that the load method of DynamoDBMapper is called once
        verify(dynamoDBMapper, times(1)).load(Restaurant.class, "Test Restaurant");

        // Verify that the retrieved restaurant is the same as the mock restaurant
        assertEquals(mockRestaurant, retrievedRestaurant);
    }
}
