package searchFood.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import searchFood.model.ReviewRequest;
import searchFood.model.ReviewResponseItem;
import searchFood.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewsFallback implements ReviewsFeignClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewsFallback.class);

    @Override
    public List<ReviewResponseItem> fetchReviews(ReviewRequest request) {
        LOGGER.info("ReviewsFallback - fetchReviews");
        //List<ReviewResponseItem> records = new ArrayList<>();
        return new ArrayList<>();
    }

    @Override
    public String fetchTestReviews() {
        LOGGER.info("ReviewsFallback - fetchTestReviews");
        return null;
    }
}
