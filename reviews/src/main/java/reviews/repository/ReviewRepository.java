package reviews.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reviews.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReviewRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRepository.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    public void setDynamoDBMapper(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }


    /**
     * Finds all items by name across all restaurants.
     *
     * @param request The name of the item to search.
     * @return The list of search results.
     */
    //public List<ReviewResponse> findItemReviews(ReviewRequest request) {
    public List<ResponseItem> findItemReviews(ReviewRequest request) {

        LOGGER.info("Beginning findItemReviews");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<ItemReview> reviewList = dynamoDBMapper.scan(ItemReview.class, scanExpression);

        //List<ReviewResponse> results = new ArrayList<>();
        List<ResponseItem> results = new ArrayList<>();

        List<ItemReview> filteredReviews = reviewList.stream()
                .filter(review -> request.getItems().stream()
                        .anyMatch(item -> item.getRestaurantName().equals(review.getRestaurantName())
                                && item.getItemName().equals(review.getItemName())))
                .collect(Collectors.toList());

        LOGGER.info("Count of filteredReviews: {}",filteredReviews.size() );

        try {
             results = filteredReviews.stream()
                    .map(review -> {
                        ResponseItem responseItem = new ResponseItem();
                        responseItem.setRestaurantName(review.getRestaurantName());
                        responseItem.setItemName(review.getItemName());
                        responseItem.setRatings(review.getRatings());

                        //ReviewResponse reviewResponse = new ReviewResponse();
                        //reviewResponse.setItems(List.of(responseItem));

                        //return reviewResponse;
                        return responseItem;
                    })
                    .collect(Collectors.toList());
            if (results.size() == 0) {
                LOGGER.warn("Reviews not found");
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while finding items by name", e);
        }
        return results;
    }

}