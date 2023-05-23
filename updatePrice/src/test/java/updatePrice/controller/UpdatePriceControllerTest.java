package updatePrice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import updatePrice.model.Menu;
import updatePrice.model.MenuList;
import updatePrice.model.Restaurant;
import updatePrice.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UpdatePriceControllerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private UpdatePriceController updatePriceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdatePrice_WithValidParameters_ReturnsSuccessfulResponse() {
        // Arrange
        String restaurantName = "Restaurant1";
        String menuItemName = "Pizza";
        String newPrice = "150.00";
        Restaurant existingRestaurant = createRestaurantWithMenuItems();
        when(restaurantRepository.getRestaurantByRestaurantName(restaurantName)).thenReturn(existingRestaurant);

        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, menuItemName, newPrice);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Price updated successfully", response.getBody());

        MenuList updatedMenuList = existingRestaurant.getMenuList();
        List<Menu> updatedItems = updatedMenuList.getItems();
        AtomicBoolean itemFound = new AtomicBoolean(false);
        updatedItems.forEach(menu -> {
            if (menu.getItemName().equals(menuItemName) && menu.getPrice().equals(newPrice)) {
                itemFound.set(true);
            }
        });
        assertEquals(true, itemFound.get());
        verify(restaurantRepository, times(1)).saveRestaurant(existingRestaurant);
    }

    @Test
    void testUpdatePrice_WithNonNumericPrice_ReturnsBadRequestResponse() {
        // Arrange
        String restaurantName = "Restaurant1";
        String menuItemName = "Pizza";
        String newPrice = "invalid";
        Restaurant existingRestaurant = createRestaurantWithMenuItems();
        when(restaurantRepository.getRestaurantByRestaurantName(restaurantName)).thenReturn(existingRestaurant);

        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, menuItemName, newPrice);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is non-numeric", response.getBody());
        verify(restaurantRepository, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void testUpdatePrice_WithInvalidPriceRange_ReturnsBadRequestResponse() {
        // Arrange
        String restaurantName = "Restaurant1";
        String menuItemName = "Pizza";
        String newPrice = "300.00";
        Restaurant existingRestaurant = createRestaurantWithMenuItems();
        when(restaurantRepository.getRestaurantByRestaurantName(restaurantName)).thenReturn(existingRestaurant);

        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, menuItemName, newPrice);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is outside allowed range 100-200", response.getBody());
        verify(restaurantRepository, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void testUpdatePrice_WithNonExistingRestaurant_ReturnsBadRequestResponse() {
        // Arrange
        String restaurantName = "NonExistingRestaurant";
        String menuItemName = "Pizza";
        String newPrice = "150.00";
        when(restaurantRepository.getRestaurantByRestaurantName(restaurantName)).thenReturn(null);

        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, menuItemName, newPrice);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Restaurant not found", response.getBody());
        verify(restaurantRepository, never()).saveRestaurant(any(Restaurant.class));
    }

    // Add more test cases to cover other scenarios

    private Restaurant createRestaurantWithMenuItems() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Restaurant1");

        MenuList menuList = new MenuList();
        List<Menu> menuItems = new ArrayList<>();
        menuItems.add(new Menu("Pizza", "9.3", "120.00"));
        menuItems.add(new Menu("Burger", "9.5", "80.00"));
        menuItems.add(new Menu("Pasta", "9.7", "150.00"));
        menuList.setItems(menuItems);

        restaurant.setMenuList(menuList);
        return restaurant;
    }
}
