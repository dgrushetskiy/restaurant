package dataload.repository;


import dataload.model.Restaurant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dataload.model.SearchRestaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class RestaurantRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantRepository.class);

    public RestaurantRepository(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        LOGGER.info("RestaurantRepository Created");
    }

    private final RabbitTemplate rabbitTemplate;


    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Saves a restaurant in the repository.
     *
     * @param restaurant The restaurant to be saved.
     * @return The saved restaurant.
     */
    public Restaurant saveRestaurant(Restaurant restaurant) {
        try {
            dynamoDBMapper.save(restaurant);

            //SearchRestaurant searchRestaurant = restaurant;

            LOGGER.info("Restaurant saved successfully: {}", restaurant.getRestaurantName());

            SearchRestaurant searchRestaurant = new SearchRestaurant();
            searchRestaurant.setRestaurantName(restaurant.getRestaurantName());
            searchRestaurant.setAddress(restaurant.getAddress());
            searchRestaurant.setMenuList(restaurant.getMenuList());
            searchRestaurant.setCreatedAt(restaurant.getCreatedAt());
            searchRestaurant.setUpdatedAt(restaurant.getUpdatedAt());
            dynamoDBMapper.save(searchRestaurant);


        } catch (Exception e) {
            LOGGER.error("Failed to save restaurant: {}", restaurant.getRestaurantName(), e);
            //throw e; // Rethrow the exception to be handled by the caller
        }
        return restaurant;
    }

}