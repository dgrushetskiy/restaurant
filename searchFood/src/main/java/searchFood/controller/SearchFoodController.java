package searchFood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import searchFood.model.SearchResult;
import searchFood.repository.RestaurantRepository;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3001") //allows cross-origin requests from the specified origin.
@RestController //class is a REST controller
@RequestMapping("/food/api/v1/user") //Mapping the controller to the specified base URL path.
public class SearchFoodController {

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
                         sort, page, size); // Calls the repository method to search for items under a restaurant name
            } else if (criteria.equalsIgnoreCase("menuitem")) {
                myList = restaurantRepository.findAllItemsbyName(criteria, criteriaValue, filter,
                        sort, page, size); // Calls the repository method to search for items by name
            } else {
                return ResponseEntity.badRequest().body("Invalid search criteria");
            }
            LOGGER.info("Search request processed successfully.");
            return ResponseEntity.ok(myList);// Returns a success response with the search results
        }catch (Exception e) {
            LOGGER.error("An error occurred while processing the search request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the search request.");
        }
    }

}
