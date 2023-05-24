package updatePrice.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import updatePrice.model.Restaurant;

@Repository
public class RestaurantRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    public void setDynamoDBMapper(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Saves a restaurant to the DynamoDB table.
     *
     * @param restaurant The restaurant object to be saved.
     * @return The saved restaurant.
     */
    public Restaurant saveRestaurant(Restaurant restaurant) {
        dynamoDBMapper.save(restaurant);
        return restaurant;
    }

    /**
     * Retrieves a restaurant by name from the DynamoDB table.
     *
     * @param restaurantName The name of the restaurant to retrieve.
     * @return The retrieved restaurant or null if not found.
     */
    public Restaurant getRestaurantByRestaurantName(String restaurantName) {
        LOGGER.info("Retrieving restaurant by name: {}", restaurantName);
        return dynamoDBMapper.load(Restaurant.class, restaurantName);
    }


}