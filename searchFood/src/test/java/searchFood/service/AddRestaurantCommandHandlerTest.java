package searchFood.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import searchFood.model.AddRestaurantCommand;
import searchFood.model.SearchRestaurant;
import searchFood.repository.RestaurantRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class AddRestaurantCommandHandlerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private Logger logger;

    private AddRestaurantCommandHandler addRestaurantCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addRestaurantCommandHandler = new AddRestaurantCommandHandler();
        addRestaurantCommandHandler.restaurantRepository = restaurantRepository;
        //addRestaurantCommandHandler.LOGGER = logger;
    }

    @Test
    void handleCommand_ValidAddRestaurantCommand_CallsSaveRestaurant() {
        // Arrange
        AddRestaurantCommand addRestaurantCommand = new AddRestaurantCommand();
        addRestaurantCommand.setRestaurantName("Restaurant 1");
        addRestaurantCommand.setAddress("123 Main Street");
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        addRestaurantCommand.setMenuList(searchRestaurant.getMenuList());
        //addRestaurantCommand.setCreatedAt(LocalDateTime.now());

        // Act
        addRestaurantCommandHandler.handleCommand(addRestaurantCommand);

        // Assert
        verify(restaurantRepository, times(1)).saveRestaurant(any(SearchRestaurant.class));
        //verify(logger, times(1)).info("AddRestaurantCommandHandler: Message received in queue addrestaurant-command");
    }

}

