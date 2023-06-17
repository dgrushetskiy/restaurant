package searchFood.service;

import searchFood.model.SearchRestaurant;
import searchFood.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceUpdateCommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceUpdateCommandHandler.class);

    @Autowired
    RestaurantRepository restaurantRepository;

    // Define a RabbitMQ listener for the "priceupdate-command" queue
    @RabbitListener(queues = "priceupdate-command")
    public void handlePriceUpdateCommand(SearchRestaurant searchRestaurant) {
        LOGGER.info("PriceUpdateCommandHandler: Message received in queue priceupdate-command");

        // Save the updated restaurant data in the repository
        restaurantRepository.saveRestaurant(searchRestaurant);
    }
}
