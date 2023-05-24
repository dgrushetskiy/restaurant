package searchFood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchFood.model.SearchResult;
import searchFood.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/food/api/v1/user")
public class SearchFoodController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFoodController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Handles the search food request based on the given criteria and criteria value.
     *
     * @param criteria      The search criteria (restaurantname or menuitem).
     * @param criteriaValue The value to search for in the specified criteria.
     * @return ResponseEntity containing the search results or error message.
     */
    @GetMapping("/{criteria}/{criteriaValue}")
    public ResponseEntity<Object> searchFood(
            @PathVariable String criteria,
            @PathVariable String criteriaValue
    ) {
        try {
            List<SearchResult> myList;
            if (criteria.equalsIgnoreCase("restaurantname")) {
                myList = restaurantRepository.findItemsUnderRestaurant(criteriaValue);
            } else if (criteria.equalsIgnoreCase("menuitem")) {
                myList = restaurantRepository.findAllItemsbyName(criteriaValue);
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
