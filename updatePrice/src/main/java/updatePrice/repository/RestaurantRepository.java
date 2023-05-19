package updatePrice.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import updatePrice.model.Restaurant;

@Repository
public class RestaurantRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Restaurant saveRestaurant(Restaurant restaurant) {
        dynamoDBMapper.save(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurantByName(String name) {
        return dynamoDBMapper.load(Restaurant.class, name);
    }


//    public Restaurant[] getAllRestaurants() {
//    }


}