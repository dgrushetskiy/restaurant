package searchFood.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import searchFood.model.ReviewRequest;
import searchFood.model.ReviewResponseItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewsFallbackTest {

    private ReviewsFallback reviewsFallback;

    @BeforeEach
    public void setUp() {
        reviewsFallback = new ReviewsFallback();
    }

    @Test
    public void fetchReviews_ReturnsEmptyList() {
        ReviewRequest request = new ReviewRequest();

        List<ReviewResponseItem> result = reviewsFallback.fetchReviews(request);

        assertEquals(0, result.size(), "The fetched reviews list should be empty");
    }
}
