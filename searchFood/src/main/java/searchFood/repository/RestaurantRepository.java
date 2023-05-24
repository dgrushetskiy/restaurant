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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        LOGGER.info("Saved restaurant: {}", restaurant.getRestaurantName());
        return restaurant;
    }

    /**
     * Retrieves a restaurant by name from the DynamoDB table.
     *
     * @param restaurantName The name of the restaurant to retrieve.
     * @return The retrieved restaurant or null if not found.
     */
    public Restaurant getRestaurantByName(String restaurantName) {
        LOGGER.info("Retrieving restaurant by name: {}", restaurantName);
        return dynamoDBMapper.load(Restaurant.class, restaurantName);
    }

    /**
     * Finds all items under a specific restaurant by name.
     *
     * @param restaurantName The name of the restaurant.
     * @return The list of search results.
     */
    public List<SearchResult> findItemsUnderRestaurant( String restaurantName){

        LOGGER.info("Finding items under restaurant: {}", restaurantName);
        Restaurant restaurant = getRestaurantByName(restaurantName);
        if (restaurant != null) {
            return mapToSearchResult(restaurant);
        } else {
            LOGGER.warn("Restaurant not found: {}", restaurantName);
            return new ArrayList<>();
        }
    }

    /**
     * Maps the menu items of a restaurant to search results.
     *
     * @param restaurant The restaurant object.
     * @return The list of search results.
     */
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
        } catch (Exception e) {
            LOGGER.error("Error occurred while mapping restaurant to search results", e);
        }
        return results;
    }

    /**
     * Finds all items by name across all restaurants.
     *
     * @param itemName The name of the item to search.
     * @return The list of search results.
     */
    public List<SearchResult> findAllItemsbyName( String itemName){
        LOGGER.info("Finding items by name: {}", itemName);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Restaurant> restaurantList =  dynamoDBMapper.scan(Restaurant.class, scanExpression);
        List<SearchResult> results = new ArrayList<>();

        try{
            results = restaurantList.stream()
                    .flatMap(restaurant -> restaurant.getMenuList().getItems().stream()
                            .filter(menu -> menu.getItemName().equals(itemName))
                            .map(menu -> mapToSearchResultByItem(menu, restaurant)))
                    .collect(Collectors.toList());
            if (results.size() == 0){
                LOGGER.warn("Item not found: {}", itemName);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while finding items by name", e);
        }
        return results;
    }

    /**
     * Maps a menu item and its associated restaurant to a search result.
     *
     * @param menu      The menu item.
     * @param restaurant The associated restaurant.
     * @return The search result.
     */
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