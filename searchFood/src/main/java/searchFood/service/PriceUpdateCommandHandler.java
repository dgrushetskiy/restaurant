package searchFood.service;


import searchFood.model.AddRestaurantCommand;
import searchFood.model.SearchRestaurant;
import searchFood.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PriceUpdateCommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceUpdateCommandHandler.class);

    @Autowired
    RestaurantRepository restaurantRepository;

    @RabbitListener(queues = "priceupdate-command")
    public void handlePriceUpdateCommand(SearchRestaurant searchRestaurant) {
        LOGGER.info("PriceUpdateCommandHandler: Message received in queue priceupdate-command");
        restaurantRepository.saveRestaurant(searchRestaurant);
    }
}
