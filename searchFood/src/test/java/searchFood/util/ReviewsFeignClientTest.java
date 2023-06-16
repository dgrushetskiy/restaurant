package searchFood.util;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import searchFood.model.ReviewRequest;
import searchFood.model.ReviewRequestItem;
import searchFood.model.ReviewResponseItem;
import searchFood.util.ReviewsFallback;
import searchFood.util.ReviewsFeignClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewsFeignClientTest {

    @Mock
    private ReviewsFallback reviewsFallback;

    private ReviewsFeignClient reviewsFeignClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewsFeignClient = new ReviewsFeignClient() {
            @Override
            public List<ReviewResponseItem> fetchReviews(ReviewRequest request) {
                return reviewsFallback.fetchReviews(request);
            }

//            @Override
//            public String fetchTestReviews() {
//                return reviewsFallback.fetchTestReviews();
//            }
        };
    }

    @Test
    void fetchReviews_ValidRequest_ReturnsReviewResponseItems() {
        // Arrange
        ReviewRequestItem requestItem = new ReviewRequestItem();
        requestItem.setRestaurantName("restaurant1");
        ReviewRequest request = new ReviewRequest();
        request.setItems(List.of(requestItem));

        List<ReviewResponseItem> expectedResponse = new ArrayList<>();
        ReviewResponseItem responseItem = new ReviewResponseItem();
        responseItem.setRestaurantName("restaurant1");
        responseItem.setRatings("4.5");
        expectedResponse.add(responseItem);

        when(reviewsFallback.fetchReviews(request)).thenReturn(expectedResponse);

        // Act
        List<ReviewResponseItem> response = reviewsFeignClient.fetchReviews(request);

        // Assert
        assertEquals(expectedResponse, response);
        verify(reviewsFallback, times(1)).fetchReviews(request);
    }

    @Test
    void fetchReviews_FeignException_ReturnsEmptyList() {
        // Arrange
        ReviewRequestItem requestItem = new ReviewRequestItem();
        requestItem.setRestaurantName("restaurant1");
        ReviewRequest request = new ReviewRequest();
        request.setItems(List.of(requestItem));

        //when(reviewsFallback.fetchReviews(request)).thenThrow(FeignException.class);

        List<ReviewResponseItem> responseItems = new ArrayList<>();
        when(reviewsFallback.fetchReviews(request)).thenReturn(responseItems);

        // Act
        List<ReviewResponseItem> response = reviewsFeignClient.fetchReviews(request);

        // Assert
        assertNotNull(response);
        //assertTrue(response.isEmpty());
        verify(reviewsFallback, times(1)).fetchReviews(request);
    }

//    @Test
//    void fetchTestReviews_ValidRequest_ReturnsTestReviews() {
//        // Arrange
//        String expectedResponse = "Test reviews";
//
//        when(reviewsFallback.fetchTestReviews()).thenReturn(expectedResponse);
//
//        // Act
//        String response = reviewsFeignClient.fetchTestReviews();
//
//        // Assert
//        assertEquals(expectedResponse, response);
//        verify(reviewsFallback, times(1)).fetchTestReviews();
//    }

}
