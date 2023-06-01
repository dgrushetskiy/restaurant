package addRestaurant.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestaurantTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        String restaurantName = "The Food Place";
        String address = "123 Main Street";
        MenuList menuList = new MenuList();

        // Act
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(restaurantName);
        restaurant.setAddress(address);
        restaurant.setMenuList(menuList);

        // Assert
        assertEquals(restaurantName, restaurant.getRestaurantName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(menuList, restaurant.getMenuList());
    }

    @Test
    void constructor_AllArgsConstructor() {
        // Create sample menu items
        Menu menu1 = new Menu("French Fries", "4.5", "10.99");
        Menu menu2 = new Menu("Pizza", "4.2", "8.99");
        Menu menu3 = new Menu("Burger", "3.7", "6.99");

        // Create a menu list with the menu items
        MenuList menuList = new MenuList(Arrays.asList(menu1, menu2, menu3));

        // Create an instance using the all-args constructor
        RestaurantRequest restaurantRequest = new RestaurantRequest("Restaurant 1", "Address 1", menuList);

        // Assert that the instance is not null
        assertNotNull(restaurantRequest);

        // Assert that the restaurant name is set correctly
        assertEquals("Restaurant 1", restaurantRequest.getRestaurantName());

        // Assert that the address is set correctly
        assertEquals("Address 1", restaurantRequest.getAddress());

        // Assert that the menu list is set correctly
        assertEquals(menuList, restaurantRequest.getMenuList());
    }
}
