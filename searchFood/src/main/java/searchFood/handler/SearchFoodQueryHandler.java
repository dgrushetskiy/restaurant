package searchFood.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import searchFood.model.SearchResult;
import searchFood.repository.RestaurantRepository;

import java.util.List;

@Component
public class SearchFoodQueryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFoodQueryHandler.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public SearchFoodQueryHandler(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitListener(queues = "searchfood-queries")
    public void handleSearchQuery(SearchFoodQuery searchQuery) {
        // Handle the search query here
        // Retrieve data from the appropriate data source based on the query parameters
        // Publish the query results to a response topic using RabbitMQ
        ResponseEntity<Object> queryResult = executeSearchQuery(searchQuery);
        amqpTemplate.convertAndSend("searchfood-query-results", queryResult.toString());
    }

    private ResponseEntity<Object>  executeSearchQuery(SearchFoodQuery searchQuery) {
        // Perform the search query execution and return the results

        try {
            List<SearchResult> myList;
            if (searchQuery.getCriteria().equalsIgnoreCase("restaurantname")) {
                myList = restaurantRepository.findItemsUnderRestaurant("restaurantname", searchQuery.getCriteriaValue(), null, null, 0, 10);
            } else if (searchQuery.getCriteria().equalsIgnoreCase("menuitem")) {
                myList = restaurantRepository.findAllItemsbyName("menuitem", searchQuery.getCriteriaValue(), null, null, 0, 10);
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
