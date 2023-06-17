package reviews.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reviews.model.ResponseItem;
import reviews.model.ReviewRequest;
import reviews.model.ReviewResponse;
import reviews.repository.ReviewRepository;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/review")
public class ReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewRepository reviewRepository;

    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/restaurantitem")
    public List<ResponseItem> findReviews(@RequestBody ReviewRequest request) {
        LOGGER.info("Start findReviews");

        try {
            // Call the repository to find reviews based on the request
            List<ResponseItem> myList = reviewRepository.findItemReviews(request);

            return myList;
        } catch (Exception e) {
            LOGGER.error("An error occurred while processing the search request.", e);
        }

        return null; // Return null if an error occurred
    }
}
