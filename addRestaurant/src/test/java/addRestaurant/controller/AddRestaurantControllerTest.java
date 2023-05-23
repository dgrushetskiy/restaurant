package addRestaurant.controller;

import addRestaurant.model.Menu;
import addRestaurant.model.MenuList;
import addRestaurant.model.Restaurant;
import addRestaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AddRestaurantControllerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private AddRestaurantController addRestaurantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRestaurant_WithValidRestaurant_ReturnsSuccessResponse() {
        // Arrange
        Restaurant restaurant = createValidRestaurant();
        when(restaurantRepository.getRestaurantByName(restaurant.getRestaurantName())).thenReturn(null);
        when(restaurantRepository.saveRestaurant(restaurant)).thenReturn(restaurant);

        // Act
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restaurant saved successfully", response.getBody());
        verify(restaurantRepository, times(1)).getRestaurantByName(restaurant.getRestaurantName());
        verify(restaurantRepository, times(1)).saveRestaurant(restaurant);
    }

    @Test
    void testAddRestaurant_WithExistingRestaurant_ReturnsBadRequestResponse() {
        // Arrange
        Restaurant restaurant = createValidRestaurant();
        when(restaurantRepository.getRestaurantByName(restaurant.getRestaurantName())).thenReturn(restaurant);

        // Act
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Restaurant already exists", response.getBody());
        verify(restaurantRepository, times(1)).getRestaurantByName(restaurant.getRestaurantName());
        verify(restaurantRepository, never()).saveRestaurant(restaurant);
    }

    // Add more test cases to cover other scenarios

    private Restaurant createValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Restaurant A");
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

        return restaurant;
    }
}
