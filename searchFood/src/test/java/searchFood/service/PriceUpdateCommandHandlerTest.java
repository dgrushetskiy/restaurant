package searchFood.service;

import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import searchFood.model.SearchRestaurant;
import searchFood.repository.RestaurantRepository;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class PriceUpdateCommandHandlerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private Logger logger;

    private PriceUpdateCommandHandler priceUpdateCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        priceUpdateCommandHandler = new PriceUpdateCommandHandler();
        priceUpdateCommandHandler.restaurantRepository = restaurantRepository;
    }

    @Test
    void handlePriceUpdateCommand_ValidSearchRestaurant_CallsSaveRestaurant() {
        // Arrange
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName("Restaurant 1");
        searchRestaurant.setAddress("123 Main Street");
        searchRestaurant.setUpdatedAt(String.valueOf(LocalDateTime.now()));

        // Act
        priceUpdateCommandHandler.handlePriceUpdateCommand(searchRestaurant);

        // Assert
        verify(restaurantRepository, times(1)).saveRestaurant(searchRestaurant);
    }

}
