package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Saves a searchRestaurant to the DynamoDB table.
     *
     * @param searchRestaurant The searchRestaurant object to be saved.
     * @return The saved searchRestaurant.
     */
    public SearchRestaurant saveRestaurant(SearchRestaurant searchRestaurant) {
        dynamoDBMapper.save(searchRestaurant);
        LOGGER.info("Saved searchRestaurant: {}", searchRestaurant.getRestaurantName());
        return searchRestaurant;
    }

    /**
     * Retrieves a restaurant by name from the DynamoDB table.
     *
     * @param restaurantName The name of the restaurant to retrieve.
     * @return The retrieved restaurant or null if not found.
     */
//    public SearchRestaurant getRestaurantByName(String restaurantName) {
//        LOGGER.info("Retrieving restaurant by name: {}", restaurantName);
//        return dynamoDBMapper.load(SearchRestaurant.class, restaurantName);
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

        LOGGER.info("Finding items under searchRestaurant: {}", restaurantName);
        SearchRestaurant searchRestaurant = dynamoDBMapper.load(SearchRestaurant.class, restaurantName);
        if (searchRestaurant != null) {
            //return mapToSearchResult(searchRestaurant);
            List<SearchResult> results = searchRestaurant.getMenuList().getItems().stream()
                    .map(menu -> {
                        //RestaurantSearchResult searchItem = new RestaurantSearchResult();
                        RestaurantSearchResult searchItem = (RestaurantSearchResult) SearchResultFactory.getSearchResult("SearchRestaurant");
                        searchItem.setName(searchRestaurant.getRestaurantName());
                        searchItem.setAddress(searchRestaurant.getAddress());
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
                //ResponseEntity<Object> response = feignClient.fetchReviews(reviewRequest);
                //List<ReviewResponseItem> fetchedReviews = (List<ReviewResponseItem>) response.getBody();
                List<ReviewResponseItem> fetchedReviews = feignClient.fetchReviews(reviewRequest);
                //LOGGER.info(feignClient.fetchTestReviews());

                for (SearchResult result : results) {
                    for (ReviewResponseItem review : fetchedReviews) {
                        if (result.getItemName().equals(review.getItemName()) && result.getName().equals(review.getRestaurantName())) {
                            result.setRatings(review.getRatings());
                            break; // Break the inner loop once a match is found
                        }else {
                            LOGGER.info("result.getItemName() = " + result.getItemName() + ", review.getItemName() = " + review.getItemName() + ", result.getName() = " + result.getName() + ", review.getRestaurantName() = " + review.getRestaurantName());
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
            LOGGER.warn("SearchRestaurant not found: {}", restaurantName);
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
//    private List<SearchResult> mapToSearchResult(SearchRestaurant restaurant) {
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
        List<SearchRestaurant> searchRestaurantList = dynamoDBMapper.scan(SearchRestaurant.class, scanExpression);
        List<SearchResult> results = new ArrayList<>();

        try {
            results = searchRestaurantList.stream()
                    .flatMap(searchRestaurant -> searchRestaurant.getMenuList().getItems().stream()
                            .filter(menu -> menu.getItemName().equals(itemName))
                            .map(menu -> mapToSearchResultByItem(menu, searchRestaurant)))
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
     * Maps a menu item and its associated searchRestaurant to a search result.
     *
     * @param menu       The menu item.
     * @param searchRestaurant The associated searchRestaurant.
     * @return The search result.
     */
    private SearchResult mapToSearchResultByItem(Menu menu, SearchRestaurant searchRestaurant) {

        //RestaurantSearchResult result = new RestaurantSearchResult();
        RestaurantSearchResult result = (RestaurantSearchResult) SearchResultFactory.getSearchResult("SearchRestaurant");

        result.setName(searchRestaurant.getRestaurantName());
        result.setAddress(searchRestaurant.getAddress());
        result.setItemName(menu.getItemName());
        result.setRatings(menu.getRatings());
        result.setPrice(menu.getPrice());

        return result;
    }
}