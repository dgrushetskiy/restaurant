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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Restaurant createValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Restaurant A");
        restaurant.setAddress("123 Main St");

        Menu menu1 = new Menu();
        menu1.setItemName("Naan");
        menu1.setRatings("9.5");
        menu1.setPrice("150.0");

        Menu menu2 = new Menu();
        menu2.setItemName("French Fries");
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

    @Test
    void addRestaurant_NonNumericRating() {
        // Create a sample restaurant with an invalid price
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Burger");
        menu.setPrice("110");
        menu.setRatings("abcd");// Invalid rating
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Rating abcd of item Item 1 is non-numeric", response.getBody());
    }

    @Test
    void addRestaurant_TooLowRating() {
        // Create a sample restaurant with an invalid price
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Pizza");
        menu.setPrice("110");
        menu.setRatings("0.1");// Invalid rating
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Rating 0.1 of item Item 1 is outside allowed range 1-10", response.getBody());
    }

    @Test
    void addRestaurant_TooHighRating() {
        // Create a sample restaurant with an invalid price
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("French Fries");
        menu.setPrice("110");
        menu.setRatings("10.1");// Invalid rating
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Rating 10.1 of item Item 1 is outside allowed range 1-10", response.getBody());
    }

    @Test
    void addRestaurant_NonNumericPrice() {
        // Create a sample restaurant with an invalid price
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Burger");
        menu.setPrice("abc"); // Invalid price
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price abc of item Item 1 is non-numeric", response.getBody());
    }

    @Test
    void addRestaurant_TooLessPrice() {
        // Create a sample restaurant with an invalid price
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Pizza");
        menu.setPrice("99"); // Invalid price
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price 99 of item Item 1 is outside allowed range 100-200", response.getBody());
    }

    @Test
    void addRestaurant_TooHighPrice() {
        // Create a sample restaurant with an invalid price
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Burger");
        menu.setPrice("201"); // Invalid price
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price 201 of item Item 1 is outside allowed range 100-200", response.getBody());
    }

    @Test
    void addRestaurant_InternalServerError() {
        // Create a sample restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName("Sample Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Pizza");
        menu.setPrice("135");
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurant.setMenuList(menuList);

        // Mock the restaurant repository's getRestaurantByName method to throw an exception
        when(restaurantRepository.getRestaurantByName("Sample Restaurant")).thenThrow(RuntimeException.class);

         // Call the controller method and assert the response
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurant);

        // Verify that the restaurant repository's getRestaurantByName method was called
        verify(restaurantRepository, times(1)).getRestaurantByName("Sample Restaurant");

        // Assert the response status code and body
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }
}
