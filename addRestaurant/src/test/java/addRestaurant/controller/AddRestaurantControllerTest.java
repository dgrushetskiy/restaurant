package addRestaurant.controller;

import addRestaurant.model.Menu;
import addRestaurant.model.MenuList;
import addRestaurant.model.Restaurant;
import addRestaurant.model.AddRestaurantCommand;
import addRestaurant.repository.RestaurantRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AddRestaurantControllerTest {

    @InjectMocks
    private AddRestaurantController addRestaurantController;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;




    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addRestaurantController = new AddRestaurantController(rabbitTemplate);
        addRestaurantController.setRestaurantRepository(restaurantRepository);
        addRestaurantController.setObjectMapper(objectMapper);
    }

    @Test
    void addRestaurant_ValidRequest_Success() throws Exception {
        AddRestaurantCommand request = new AddRestaurantCommand();
        request.setRestaurantName("Test Restaurant");
        request.setAddress("Test Address");

        Menu menu1 = new Menu();
        menu1.setItemName("Pizza");
        menu1.setPrice("150.0");
        menu1.setRatings("8.5");

        Menu menu2 = new Menu();
        menu2.setItemName("Burger");
        menu2.setPrice("120.0");
        menu2.setRatings("7.8");

        List<Menu> menuItems = Arrays.asList(menu1, menu2);
        MenuList menuList = new MenuList();
        menuList.setItems(menuItems);
        request.setMenuList(menuList);

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setRestaurantName("Test Restaurant");
        savedRestaurant.setAddress("Test Address");
        savedRestaurant.setMenuList(menuList);
        savedRestaurant.setCreatedAt(String.valueOf(LocalDateTime.now()));

        when(restaurantRepository.getRestaurantByName("Test Restaurant")).thenReturn(null);
        when(restaurantRepository.saveRestaurant(any(Restaurant.class))).thenReturn(savedRestaurant);
        when(objectMapper.writeValueAsString(request)).thenReturn("json");

        ResponseEntity<String> response = addRestaurantController.addRestaurant(request);

        verify(restaurantRepository, times(1)).getRestaurantByName("Test Restaurant");
        verify(restaurantRepository, times(1)).saveRestaurant(any(Restaurant.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("addrestaurant-command"), any(Message.class));
        verify(objectMapper, times(1)).writeValueAsString(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restaurant saved successfully", response.getBody());
    }

    @Test
    void testAddRestaurant_WithValidRestaurant_ReturnsSuccessResponse() throws JsonProcessingException {
        // Arrange
        AddRestaurantCommand restaurantRequest = createValidRestaurant();
        when(restaurantRepository.getRestaurantByName(restaurantRequest.getRestaurantName())).thenReturn(null);

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(restaurantRequest.getRestaurantName());
        restaurant.setMenuList(restaurantRequest.getMenuList());
        restaurant.setAddress(restaurantRequest.getAddress());

        when(restaurantRepository.saveRestaurant(restaurant)).thenReturn(restaurant);
        when(objectMapper.writeValueAsString(restaurantRequest)).thenReturn("json");

        // Act
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restaurant saved successfully", response.getBody());
        verify(restaurantRepository, times(1)).getRestaurantByName(restaurant.getRestaurantName());
        verify(restaurantRepository, times(1)).saveRestaurant(any());
    }

    @Test
    void testAddRestaurant_WithExistingRestaurant_ReturnsBadRequestResponse() {
        // Arrange
        AddRestaurantCommand restaurantRequest = createValidRestaurant();
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.getRestaurantByName(restaurantRequest.getRestaurantName())).thenReturn(restaurant);

        // Act
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Restaurant already exists", response.getBody());
        verify(restaurantRepository, times(1)).getRestaurantByName(restaurantRequest.getRestaurantName());
        verify(restaurantRepository, never()).saveRestaurant(restaurant);
    }

    private AddRestaurantCommand createValidRestaurant() {
        AddRestaurantCommand restaurant = new AddRestaurantCommand();
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
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Burger");
        menu.setPrice("110");
        menu.setRatings("abcd");// Invalid rating
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Rating abcd of item Burger is non-numeric", response.getBody());
    }

    @Test
    void addRestaurant_TooLowRating() {
        // Create a sample restaurant with an invalid price
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Pizza");
        menu.setPrice("110");
        menu.setRatings("0.1");// Invalid rating
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Rating 0.1 of item Pizza is outside allowed range 1-10", response.getBody());
    }

    @Test
    void addRestaurant_TooHighRating() {
        // Create a sample restaurant with an invalid price
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("French Fries");
        menu.setPrice("110");
        menu.setRatings("10.1");// Invalid rating
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Rating 10.1 of item French Fries is outside allowed range 1-10", response.getBody());
    }

    @Test
    void addRestaurant_NonNumericPrice() {
        // Create a sample restaurant with an invalid price
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Burger");
        menu.setPrice("abc"); // Invalid price
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price abc of item Burger is non-numeric", response.getBody());
    }

    @Test
    void addRestaurant_TooLessPrice() {
        // Create a sample restaurant with an invalid price
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Pizza");
        menu.setPrice("99"); // Invalid price
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price 99 of item Pizza is outside allowed range 100-200", response.getBody());
    }

    @Test
    void addRestaurant_TooHighPrice() {
        // Create a sample restaurant with an invalid price
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("New Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Burger");
        menu.setPrice("201"); // Invalid price
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Call the controller method
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the response is a bad request with the expected error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price 201 of item Burger is outside allowed range 100-200", response.getBody());
    }

    @Test
    void addRestaurant_InternalServerError() {
        // Create a sample restaurant
        AddRestaurantCommand restaurantRequest = new AddRestaurantCommand();
        restaurantRequest.setRestaurantName("Sample Restaurant");

        MenuList menuList = new MenuList();
        List<Menu> items = new ArrayList<>();
        Menu menu = new Menu();
        menu.setItemName("Pizza");
        menu.setPrice("135");
        menu.setRatings("8.5");
        items.add(menu);
        menuList.setItems(items);
        restaurantRequest.setMenuList(menuList);

        // Mock the restaurant repository's getRestaurantByName method to throw an exception
        when(restaurantRepository.getRestaurantByName("Sample Restaurant")).thenThrow(RuntimeException.class);

         // Call the controller method and assert the response
        ResponseEntity<String> response = addRestaurantController.addRestaurant(restaurantRequest);

        // Verify that the restaurant repository's getRestaurantByName method was called
        verify(restaurantRepository, times(1)).getRestaurantByName("Sample Restaurant");

        // Assert the response status code and body
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }
}
