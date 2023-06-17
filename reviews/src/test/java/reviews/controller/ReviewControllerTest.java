package reviews.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reviews.model.ResponseItem;
import reviews.model.ReviewRequest;
import reviews.model.ReviewResponse;
import reviews.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    private ReviewController reviewController;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewController = new ReviewController();
        reviewController.setReviewRepository(reviewRepository);
    }

    @Test
    void findReviews_ValidRequest_ReturnsResponseItemList() {
        // Arrange
        ReviewRequest request = new ReviewRequest();
        List<ResponseItem> expectedList = new ArrayList<>();
        when(reviewRepository.findItemReviews(request)).thenReturn(expectedList);

        // Act
        List<ResponseItem> actualList = reviewController.findReviews(request);

        // Assert
        assertEquals(expectedList, actualList);
        verify(reviewRepository, times(1)).findItemReviews(request);
    }

    @Test
    void findReviews_ExceptionThrown_ReturnsNull() {
        // Arrange
        ReviewRequest request = new ReviewRequest();
        when(reviewRepository.findItemReviews(request)).thenThrow(new RuntimeException("Test Exception"));

        // Act
        List<ResponseItem> actualList = reviewController.findReviews(request);

        // Assert
        assertNull(actualList);
        verify(reviewRepository, times(1)).findItemReviews(request);
    }
}
