package reviews.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reviews.model.RequestItem;
import reviews.model.ResponseItem;
import reviews.model.ReviewRequest;
import reviews.model.ItemReview;
import reviews.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewRepositoryTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRepository.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewRepository = new ReviewRepository();
        reviewRepository.setDynamoDBMapper(dynamoDBMapper);
    }

    @Test
    void findItemReviews_ValidRequest_ReturnsResponseItemList() {
        // Arrange
        ReviewRequest request = new ReviewRequest();
        ItemReview review1 = new ItemReview();
        review1.setRestaurantName("Restaurant 1");
        review1.setItemName("Item 1");
        review1.setRatings("4.5");

        ItemReview review2 = new ItemReview();
        review2.setRestaurantName("Restaurant 2");
        review2.setItemName("Item 2");
        review2.setRatings("3.8");

        List<ItemReview> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);


        //PaginatedScanList<ItemReview> reviewList = new PaginatedScanList<ItemReview>();

        when(dynamoDBMapper.scan(ItemReview.class, new DynamoDBScanExpression())).thenReturn(mock(PaginatedScanList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewList))));
        //when(dynamoDBMapper.scan(ItemReview.class, Mockito.any())).thenReturn(mock(PaginatedScanList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewList))));

        //PaginatedScanList<ItemReview> mockPaginatedList = reviewList;
        //when(dynamoDBMapper.scan(eq(ItemReview.class),
        //        Mockito.any())).thenReturn(mockPaginatedList);


        // Act
        List<ResponseItem> actualList = reviewRepository.findItemReviews(request);

        // Assert
        assertNotNull(actualList);
        assertEquals(2, actualList.size());
        assertEquals("Restaurant 1", actualList.get(0).getRestaurantName());
        assertEquals("Item 1", actualList.get(0).getItemName());
        assertEquals(4.5, actualList.get(0).getRatings());
        assertEquals("Restaurant 2", actualList.get(1).getRestaurantName());
        assertEquals("Item 2", actualList.get(1).getItemName());
        assertEquals(3.8, actualList.get(1).getRatings());

        verify(dynamoDBMapper, times(1)).scan(ItemReview.class, new DynamoDBScanExpression());
    }

    @Test
    void findItemReviews_EmptyReviewList_ReturnsEmptyResponseItemList() {
        // Arrange
        ReviewRequest request = new ReviewRequest();
        ArrayList<ItemReview> reviewList = new ArrayList<>();
        //when(dynamoDBMapper.scan(ItemReview.class, new DynamoDBScanExpression())).thenReturn(reviewList);
        when(dynamoDBMapper.scan(ItemReview.class, new DynamoDBScanExpression())).thenReturn(mock(PaginatedScanList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewList))));


        // Act
        List<ResponseItem> actualList = reviewRepository.findItemReviews(request);

        // Assert
        assertNotNull(actualList);
        assertTrue(actualList.isEmpty());

        verify(dynamoDBMapper, times(1)).scan(ItemReview.class, new DynamoDBScanExpression());
        verify(LOGGER, times(1)).warn("Reviews not found");
    }

    @Test
    void findItemReviews_FilteredReviews_ReturnsFilteredResponseItemList() {
        // Arrange
        ReviewRequest request = new ReviewRequest();
        RequestItem item1 = new RequestItem();
        item1.setRestaurantName("Restaurant 1");
        item1.setItemName("Item 1");

        RequestItem item2 = new RequestItem();
        item2.setRestaurantName("Restaurant 2");
        item2.setItemName("Item 2");

        request.getItems().add(item1);
        request.getItems().add(item2);

        ItemReview review1 = new ItemReview();
        review1.setRestaurantName("Restaurant 1");
        review1.setItemName("Item 1");
        review1.setRatings("4.5");

        ItemReview review2 = new ItemReview();
        review2.setRestaurantName("Restaurant 2");
        review2.setItemName("Item 2");
        review2.setRatings("3.8");

        ItemReview review3 = new ItemReview();
        review3.setRestaurantName("Restaurant 3");
        review3.setItemName("Item 3");
        review3.setRatings("4.0");

        List<ItemReview> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);
        reviewList.add(review3);

        //when(dynamoDBMapper.scan(ItemReview.class, new DynamoDBScanExpression())).thenReturn(reviewList);
        when(dynamoDBMapper.scan(ItemReview.class, new DynamoDBScanExpression())).thenReturn(mock(PaginatedScanList.class, withSettings().defaultAnswer(new ForwardsInvocations(reviewList))));

        // Act
        List<ResponseItem> actualList = reviewRepository.findItemReviews(request);

        // Assert
        assertNotNull(actualList);
        assertEquals(2, actualList.size());
        assertEquals("Restaurant 1", actualList.get(0).getRestaurantName());
        assertEquals("Item 1", actualList.get(0).getItemName());
        assertEquals(4.5, actualList.get(0).getRatings());
        assertEquals("Restaurant 2", actualList.get(1).getRestaurantName());
        assertEquals("Item 2", actualList.get(1).getItemName());
        assertEquals(3.8, actualList.get(1).getRatings());

        verify(dynamoDBMapper, times(1)).scan(ItemReview.class, new DynamoDBScanExpression());
        verify(LOGGER, times(1)).info("Beginning findItemReviews");
        verify(LOGGER, times(1)).info("Count of filteredReviews: 2");
    }

    @Test
    void findItemReviews_ExceptionThrown_ReturnsEmptyResponseItemList() {
        // Arrange
        ReviewRequest request = new ReviewRequest();
        when(dynamoDBMapper.scan(ItemReview.class, new DynamoDBScanExpression())).thenThrow(new RuntimeException("Test Exception"));

        // Act
        List<ResponseItem> actualList = reviewRepository.findItemReviews(request);

        // Assert
        assertNotNull(actualList);
        assertTrue(actualList.isEmpty());

        verify(dynamoDBMapper, times(1)).scan(ItemReview.class, new DynamoDBScanExpression());
        verify(LOGGER, times(1)).error("Error occurred while finding items by name", (Object) any());
    }
}
