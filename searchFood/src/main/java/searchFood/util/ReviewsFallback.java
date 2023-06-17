package searchFood.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import searchFood.model.ReviewRequest;
import searchFood.model.ReviewResponseItem;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewsFallback implements ReviewsFeignClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewsFallback.class);

    @Override
    public List<ReviewResponseItem> fetchReviews(ReviewRequest request) {
        // Return an empty list as a fallback response for fetching reviews
        return new ArrayList<>();
    }
}
