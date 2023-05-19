package addrestaurant.repository;

import addrestaurant.model.Restaurant;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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