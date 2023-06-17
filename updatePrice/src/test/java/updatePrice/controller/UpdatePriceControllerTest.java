package updatePrice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import updatePrice.model.Menu;
import updatePrice.model.MenuList;
import updatePrice.model.PriceUpdateRequest;
import updatePrice.model.Restaurant;
import updatePrice.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UpdatePriceControllerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UpdatePriceController updatePriceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updatePriceController = new UpdatePriceController(rabbitTemplate);
        updatePriceController.setRestaurantRepository(restaurantRepository);
        updatePriceController.setObjectMapper(objectMapper);
    }

    @Test
    void updatePrice_ValidRequest_ReturnsOkResponse() throws Exception {
        // Arrange
        String restaurantName = "Restaurant A";
        String menuItemName = "Pizza";
        String newPrice = "150.00";

        PriceUpdateRequest priceUpdateRequest = new PriceUpdateRequest();
        priceUpdateRequest.setMenuItemName(menuItemName);
        priceUpdateRequest.setNewPrice(newPrice);

        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setRestaurantName(restaurantName);
        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName(menuItemName);
        menu.setPrice("100.00");
        items.add(menu);
        menuList.setItems(items);
        existingRestaurant.setMenuList(menuList);

        when(restaurantRepository.getRestaurantByRestaurantName(restaurantName)).thenReturn(existingRestaurant);
        when(objectMapper.writeValueAsString(any())).thenReturn("json");

        // Act
        ResponseEntity<String> responseEntity = updatePriceController.updatePrice(restaurantName, priceUpdateRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Price updated successfully", responseEntity.getBody());
        assertEquals(newPrice, items.get(0).getPrice());
        verify(restaurantRepository, times(1)).getRestaurantByRestaurantName(restaurantName);
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
        PriceUpdateRequest priceUpdateRequest = new PriceUpdateRequest(menuItemName, newPrice);
        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, priceUpdateRequest);

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
        PriceUpdateRequest priceUpdateRequest = new PriceUpdateRequest(menuItemName, newPrice);
        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, priceUpdateRequest);

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
        PriceUpdateRequest priceUpdateRequest = new PriceUpdateRequest(menuItemName, newPrice);
        // Act
        ResponseEntity<String> response = updatePriceController.updatePrice(restaurantName, priceUpdateRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Restaurant not found", response.getBody());
        verify(restaurantRepository, never()).saveRestaurant(any(Restaurant.class));
    }

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
