package searchFood.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchRestaurantTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        String restaurantName = "The Food Place";
        String address = "123 Main Street";
        MenuList menuList = new MenuList();

        // Act
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName(restaurantName);
        searchRestaurant.setAddress(address);
        searchRestaurant.setMenuList(menuList);

        // Assert
        assertEquals(restaurantName, searchRestaurant.getRestaurantName());
        assertEquals(address, searchRestaurant.getAddress());
        assertEquals(menuList, searchRestaurant.getMenuList());
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
        RestaurantRequest restaurantRequest = new RestaurantRequest("SearchRestaurant 1", "Address 1", menuList);

        // Assert that the instance is not null
        assertNotNull(restaurantRequest);

        // Assert that the restaurant name is set correctly
        assertEquals("SearchRestaurant 1", restaurantRequest.getRestaurantName());

        // Assert that the address is set correctly
        assertEquals("Address 1", restaurantRequest.getAddress());

        // Assert that the menu list is set correctly
        assertEquals(menuList, restaurantRequest.getMenuList());
    }
}
