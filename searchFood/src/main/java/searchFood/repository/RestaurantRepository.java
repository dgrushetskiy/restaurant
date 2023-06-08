package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import searchFood.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import searchFood.util.ReviewsFeignClient;

@Repository
public class RestaurantRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private ReviewsFeignClient feignClient;

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
//    public Restaurant saveRestaurant(Restaurant restaurant) {
//        dynamoDBMapper.save(restaurant);
//        LOGGER.info("Saved restaurant: {}", restaurant.getRestaurantName());
//        return restaurant;
//    }

    /**
     * Retrieves a restaurant by name from the DynamoDB table.
     *
     * @param restaurantName The name of the restaurant to retrieve.
     * @return The retrieved restaurant or null if not found.
     */
//    public Restaurant getRestaurantByName(String restaurantName) {
//        LOGGER.info("Retrieving restaurant by name: {}", restaurantName);
//        return dynamoDBMapper.load(Restaurant.class, restaurantName);
//    }

    /**
     * Finds all items under a specific restaurant by name.
     *
     * @param restaurantName The name of the restaurant.
     * @return The list of search results.
     */
    public List<SearchResult> findItemsUnderRestaurant(String criteria, String restaurantName, String filter,
                                                       String sort,
                                                       int page,
                                                       int size) {

        LOGGER.info("Finding items under restaurant: {}", restaurantName);
        Restaurant restaurant = dynamoDBMapper.load(Restaurant.class, restaurantName);
        if (restaurant != null) {
            //return mapToSearchResult(restaurant);
            List<SearchResult> results = restaurant.getMenuList().getItems().stream()
                    .map(menu -> {
                        //RestaurantSearchResult searchItem = new RestaurantSearchResult();
                        RestaurantSearchResult searchItem = (RestaurantSearchResult) SearchResultFactory.getSearchResult("Restaurant");
                        searchItem.setName(restaurant.getRestaurantName());
                        searchItem.setAddress(restaurant.getAddress());
                        searchItem.setItemName(menu.getItemName());
                        searchItem.setRatings(menu.getRatings());
                        searchItem.setPrice(menu.getPrice());
                        return searchItem;
                    })
                    .collect(Collectors.toList());

            List<ReviewRequestItem> reviewRequestItems = new ArrayList<>();

            for (SearchResult result : results) {
                ReviewRequestItem reviewRequestItem = new ReviewRequestItem();
                reviewRequestItem.setRestaurantName(result.getName());
                reviewRequestItem.setItemName(result.getItemName());
                reviewRequestItems.add(reviewRequestItem);
            }

            ReviewRequest reviewRequest  = new ReviewRequest();
            reviewRequest.setItems(reviewRequestItems);
            try{
                ResponseEntity<Object> response = feignClient.fetchReviews(reviewRequest);
                List<ReviewResponseItem> fetchedReviews = (List<ReviewResponseItem>) response.getBody();

                //LOGGER.info(feignClient.fetchTestReviews());

                for (SearchResult result : results) {
                    for (ReviewResponseItem review : fetchedReviews) {
                        if (result.getItemName().equals(review.getItemName()) && result.getName().equals(review.getRestaurantName())) {
                            result.setRatings(review.getRatings());
                            break; // Break the inner loop once a match is found
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (filter != null && !filter.isEmpty()) {
                results = results.stream()
                        .filter(result -> containsKeyword(result, filter))
                        .collect(Collectors.toList());
            }

            if (sort != null && !sort.isEmpty()) {
                results = sortResultsByField(results, sort);
            }

            // Apply pagination
            int start = page * size;
            int end = Math.min(start + size, results.size());
            results = results.subList(start, end);

            return results;


        } else {
            LOGGER.warn("Restaurant not found: {}", restaurantName);
            return new ArrayList<>();
        }
    }

    private static boolean containsKeyword(SearchResult result, String keyword) {
        return result.getName().contains(keyword) ||
                result.getAddress().contains(keyword) ||
                result.getItemName().contains(keyword) ||
                result.getRatings().contains(keyword) ||
                result.getPrice().contains(keyword);
    }


    public static List<SearchResult> sortResultsByField(List<SearchResult> searchResults, String field) {
        Comparator<SearchResult> comparator;

        switch (field) {
            case "restaurantName":
                comparator = Comparator.comparing(SearchResult::getName);
                break;
            case "address":
                comparator = Comparator.comparing(SearchResult::getAddress);
                break;
            case "itemName":
                comparator = Comparator.comparing(SearchResult::getItemName);
                break;
            case "ratings":
                comparator = Comparator.comparing(SearchResult::getRatings);
                break;
            case "price":
                comparator = Comparator.comparing(SearchResult::getPrice);
                break;
            default:
                throw new IllegalArgumentException("Invalid field for sorting: " + field);
        }

        searchResults.sort(comparator);
        return searchResults;
    }

    /**
     * Maps the menu items of a restaurant to search results.
     *
     * @param restaurant The restaurant object.
     * @return The list of search results.
     */
//    private List<SearchResult> mapToSearchResult(Restaurant restaurant) {
//
//                 List<SearchResult> results = restaurant.getMenuList().getItems().stream()
//                .map(menu -> {
//                    SearchResult searchItem = new SearchResult();
//                    searchItem.setRestaurantName(restaurant.getRestaurantName());
//                    searchItem.setAddress(restaurant.getAddress());
//                    searchItem.setItemName(menu.getItemName());
//                    searchItem.setRatings(menu.getRatings());
//                    searchItem.setPrice(menu.getPrice());
//                    return searchItem;
//                })
//                .collect(Collectors.toList());
//
//                 return results;

//        List<SearchResult> results = new ArrayList<>();
//        try {
//            MenuList menuList = restaurant.getMenuList();
//            List<Menu> items = menuList.getItems();
//            for (Menu menu : items) {
//
//                SearchResult searchItem = new SearchResult();
//                searchItem.setRestaurantName(restaurant.getRestaurantName());
//                searchItem.setAddress(restaurant.getAddress());
//                searchItem.setItemName(menu.getItemName());
//                searchItem.setRatings(menu.getRatings());
//                searchItem.setPrice(menu.getPrice());
//                results.add(searchItem);
//
//            }
//        } catch (Exception e) {
//            LOGGER.error("Error occurred while mapping restaurant to search results", e);
//        }
//        return results;
//    }

    /**
     * Finds all items by name across all restaurants.
     *
     * @param itemName The name of the item to search.
     * @return The list of search results.
     */
    public List<SearchResult> findAllItemsbyName(String criteria, String itemName, String filter,
                                                 String sort,
                                                 int page,
                                                 int size) {
        LOGGER.info("Finding items by name: {}", itemName);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Restaurant> restaurantList = dynamoDBMapper.scan(Restaurant.class, scanExpression);
        List<SearchResult> results = new ArrayList<>();

        try {
            results = restaurantList.stream()
                    .flatMap(restaurant -> restaurant.getMenuList().getItems().stream()
                            .filter(menu -> menu.getItemName().equals(itemName))
                            .map(menu -> mapToSearchResultByItem(menu, restaurant)))
                    .collect(Collectors.toList());
            if (results.size() == 0) {
                LOGGER.warn("Item not found: {}", itemName);
            }else{
                if (filter != null && !filter.isEmpty()) {
                    results = results.stream()
                            .filter(result -> containsKeyword(result, filter))
                            .collect(Collectors.toList());
                }

                if (sort != null && !sort.isEmpty()) {
                    results = sortResultsByField(results, sort);
                }

                // Apply pagination
                int start = page * size;
                int end = Math.min(start + size, results.size());
                results = results.subList(start, end);

            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while finding items by name", e);
        }
        return results;
    }

    /**
     * Maps a menu item and its associated restaurant to a search result.
     *
     * @param menu       The menu item.
     * @param restaurant The associated restaurant.
     * @return The search result.
     */
    private SearchResult mapToSearchResultByItem(Menu menu, Restaurant restaurant) {

        //RestaurantSearchResult result = new RestaurantSearchResult();
        RestaurantSearchResult result = (RestaurantSearchResult) SearchResultFactory.getSearchResult("Restaurant");

        result.setName(restaurant.getRestaurantName());
        result.setAddress(restaurant.getAddress());
        result.setItemName(menu.getItemName());
        result.setRatings(menu.getRatings());
        result.setPrice(menu.getPrice());

        return result;
    }
}