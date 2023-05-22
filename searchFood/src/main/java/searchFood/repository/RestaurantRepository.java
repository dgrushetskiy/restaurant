package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import searchFood.model.Menu;
import searchFood.model.MenuList;
import searchFood.model.Restaurant;
import searchFood.model.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Restaurant saveRestaurant(Restaurant restaurant) {
        dynamoDBMapper.save(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        return dynamoDBMapper.load(Restaurant.class, restaurantName);
    }

    public List<SearchResult> findItemsUnderRestaurant( String restaurantName){

//        List<SearchResult> results = null;
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        List<Restaurant> restaurantList =  dynamoDBMapper.scan(Restaurant.class, scanExpression);
//
//        restaurantList = restaurantList.stream()
//                .filter(result -> result.getRestaurantName().equals(criteriaValue))
//                .collect(Collectors.toList());
        return mapToSearchResult(getRestaurantByName(restaurantName));

    }

    private List<SearchResult> mapToSearchResult(Restaurant restaurant) {

        List<SearchResult> results = new ArrayList<>();
        try {
            MenuList menuList = restaurant.getMenuList();
            List<Menu> items = menuList.getItems();
            for (Menu menu : items) {

                SearchResult searchItem = new SearchResult();
                searchItem.setRestaurantName(restaurant.getRestaurantName());
                searchItem.setAddress(restaurant.getAddress());
                searchItem.setItemName(menu.getItemName());
                searchItem.setRatings(menu.getRatings());
                searchItem.setPrice(menu.getPrice());
                results.add(searchItem);

            }
        } catch (Exception e){

        }


        return results;
    }

    public List<SearchResult> findAllItemsbyName( String itemName){

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Restaurant> restaurantList =  dynamoDBMapper.scan(Restaurant.class, scanExpression);
        List<SearchResult> results = new ArrayList<>();

        try{
            results = restaurantList.stream()
                    .flatMap(restaurant -> restaurant.getMenuList().getItems().stream()
                            .filter(menu -> menu.getItemName().equals(itemName))
                            .map(menu -> mapToSearchResultByItem(menu, restaurant)))
                    .collect(Collectors.toList());
        } catch (Exception e){

        }
        return results;

//        return restaurantList.stream()
//                .flatMap(restaurant -> restaurant.getMenuList().getItems().stream())
//                .filter(menu -> menu.getItemName().equals(itemName))
//                .map(menu -> mapToSearchResult(menu, menu.getMenuList().getRestaurant()))
//                .map(this::mapToSearchResultByItem)
//                .collect(Collectors.toList());
    }

    private SearchResult mapToSearchResultByItem(Menu menu, Restaurant restaurant) {

        SearchResult result = new SearchResult();
        result.setRestaurantName(restaurant.getRestaurantName());
        result.setAddress(restaurant.getAddress());
        result.setItemName(menu.getItemName());
        result.setRatings(menu.getRatings());
        result.setPrice(menu.getPrice());

        return result;
    }
}