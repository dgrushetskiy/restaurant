package searchFood.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import searchFood.handler.SearchFoodQuery;
//import searchFood.handler.SearchFoodQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import searchFood.model.SearchResult;
import searchFood.repository.RestaurantRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/food/api/v1/user")
public class SearchFoodController {

//    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFoodController.class);
//
//    private final AmqpTemplate amqpTemplate;
//
//    @Autowired
//    public SearchFoodController(AmqpTemplate amqpTemplate) {
//        this.amqpTemplate = amqpTemplate;
//    }
//
//    /**
//     * Handles the search food request based on the given criteria and criteria value.
//     *
//     * @param criteria      The search criteria (restaurantname or menuitem).
//     * @param criteriaValue The value to search for in the specified criteria.
//     * @return ResponseEntity containing the search results or error message.
//     */
//    @GetMapping("/{criteria}/{criteriaValue}")
//    public ResponseEntity<Object> searchFood(
//            @PathVariable String criteria,
//            @PathVariable String criteriaValue
//    ) {
//        try {
//            if (criteria.equalsIgnoreCase("restaurantname") || criteria.equalsIgnoreCase("menuitem")) {
//                // Send the search query as a message to the searchfood-queries queue
//                SearchFoodQuery searchQuery = new SearchFoodQuery(criteria, criteriaValue);
//                amqpTemplate.convertAndSend("searchfood-queries", searchQuery);
//
//                // Wait for the query result message
//                Object obj = amqpTemplate.receiveAndConvert("searchfood-query-results");
//                if (obj instanceof SearchFoodQueryResult){
//                    SearchFoodQueryResult queryResult = (SearchFoodQueryResult) amqpTemplate.receiveAndConvert("searchfood-query-results");
//
//                    LOGGER.info("Search request processed successfully.");
//                    return ResponseEntity.ok(queryResult.getSearchResults());
//                }else {
//                    return ResponseEntity.ok().body("No records found");
//                }
//
//            } else {
//                return ResponseEntity.badRequest().body("Invalid search criteria");
//            }
//        } catch (Exception e) {
//            LOGGER.error("An error occurred while processing the search request.", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the search request.");
//        }
//    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFoodController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/{criteria}/{criteriaValue}")
    public ResponseEntity<Object> searchFood(
            @PathVariable String criteria,
            @PathVariable String criteriaValue,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size

    ) {
        try {
            List<SearchResult> myList;
            if (criteria.equalsIgnoreCase("restaurantname")) {
                myList = restaurantRepository.findItemsUnderRestaurant(criteria, criteriaValue, filter,
                         sort, page, size);
            } else if (criteria.equalsIgnoreCase("menuitem")) {
                myList = restaurantRepository.findAllItemsbyName(criteria, criteriaValue, filter,
                        sort, page, size);
            } else {
                return ResponseEntity.badRequest().body("Invalid search criteria");
            }
            LOGGER.info("Search request processed successfully.");
            return ResponseEntity.ok(myList);
        }catch (Exception e) {
            LOGGER.error("An error occurred while processing the search request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the search request.");
        }
    }

}
