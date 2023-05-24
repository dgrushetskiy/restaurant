package searchFood.model;

import org.junit.jupiter.api.Test;

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
        Menu menu1 = new Menu("Item 1", "4.5", "10.99");
        Menu menu2 = new Menu("Item 2", "4.2", "8.99");
        Menu menu3 = new Menu("Item 3", "3.7", "6.99");

        // Create a menu list with the menu items
        MenuList menuList = new MenuList(Arrays.asList(menu1, menu2, menu3));

        // Create an instance using the all-args constructor
        Restaurant restaurant = new Restaurant("Restaurant 1", "Address 1", menuList);

        // Assert that the instance is not null
        assertNotNull(restaurant);

        // Assert that the restaurant name is set correctly
        assertEquals("Restaurant 1", restaurant.getRestaurantName());

        // Assert that the address is set correctly
        assertEquals("Address 1", restaurant.getAddress());

        // Assert that the menu list is set correctly
        assertEquals(menuList, restaurant.getMenuList());
    }
}
