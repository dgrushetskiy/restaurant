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
public class AddRestaurantCommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRestaurantCommandHandler.class);

    @Autowired
    RestaurantRepository restaurantRepository;


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = "addrestaurant-command")
    public void handleCommand(AddRestaurantCommand restaurantRequest) {
        LOGGER.info("AddRestaurantCommandHandler: Message received in queue addrestaurant-command");
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName(restaurantRequest.getRestaurantName());
        searchRestaurant.setAddress(restaurantRequest.getAddress());
        searchRestaurant.setMenuList(restaurantRequest.getMenuList());
        searchRestaurant.setCreatedAt(String.valueOf(LocalDateTime.now()));

        restaurantRepository.saveRestaurant(searchRestaurant);
    }
}
