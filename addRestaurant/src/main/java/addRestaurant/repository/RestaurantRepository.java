package addRestaurant.repository;

import addRestaurant.model.Restaurant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class RestaurantRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantRepository.class);

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
            LOGGER.info("Restaurant saved successfully: {}", restaurant.getRestaurantName());
        } catch (Exception e) {
            LOGGER.error("Failed to save restaurant: {}", restaurant.getRestaurantName(), e);
            throw e; // Rethrow the exception to be handled by the caller
        }
        return restaurant;
    }

    /**
     * Retrieves a restaurant by its name.
     *
     * @param name The name of the restaurant.
     * @return The restaurant with the given name, or null if not found.
     */
    public Restaurant getRestaurantByName(String name) {
        try {
            return dynamoDBMapper.load(Restaurant.class, name);
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve restaurant by name: {}", name, e);
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

}