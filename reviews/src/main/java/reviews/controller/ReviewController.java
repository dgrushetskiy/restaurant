package reviews.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/restaurantitem")
    public ResponseEntity<Object> findReviews(@RequestBody ReviewRequest request) {
        LOGGER.info("Start findReviews");

        try {
            List<ReviewResponse> myList = reviewRepository.findItemReviews(request);
            LOGGER.info("Request processed successfully.");
            return ResponseEntity.ok(myList);
        }catch (Exception e) {
            LOGGER.error("An error occurred while processing the search request.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the search request.");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Object> testReviews() {
        LOGGER.info("Start testReviews");

        LOGGER.info("Request processed successfully.");
        return ResponseEntity.ok("Processed Successfully");
    }
}
