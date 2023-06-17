package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import searchFood.model.*;
import searchFood.util.ReviewsFeignClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private ReviewsFeignClient feignClient;

    @Mock
    private Logger logger;

    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantRepository = new RestaurantRepository();
        restaurantRepository.setDynamoDBMapper(dynamoDBMapper);
        restaurantRepository.setFeignClient(feignClient);
    }

    @Test
    void saveRestaurant_ValidSearchRestaurant_CallsDynamoDBMapperSave() {
        // Arrange
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName("Restaurant 1");
        searchRestaurant.setAddress("123 Main Street");
        MenuList menuList = new MenuList();
        List<Menu> menuItems = new ArrayList<>();
        Menu menu1 = new Menu();
        menu1.setItemName("Item 1");
        menu1.setRatings("4.5");
        menu1.setPrice("$10.99");
        menuItems.add(menu1);
        menuList.setItems(menuItems);

        searchRestaurant.setMenuList(menuList);

        // Act
        restaurantRepository.saveRestaurant(searchRestaurant);

        // Assert
        verify(dynamoDBMapper, times(1)).save(searchRestaurant);
    }

    @Test
    void findItemsUnderRestaurant_ValidRestaurantName_CallsDynamoDBMapperLoad() {
        // Arrange
        String restaurantName = "Restaurant 1";
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName(restaurantName);
        searchRestaurant.setAddress("123 Main Street");
        MenuList menuList = new MenuList();
        List<Menu> menuItems = new ArrayList<>();
        Menu menu1 = new Menu();
        menu1.setItemName("Item 1");
        menu1.setRatings("4.5");
        menu1.setPrice("$10.99");
        menuItems.add(menu1);
        menuList.setItems(menuItems);

        searchRestaurant.setMenuList(menuList);
        when(dynamoDBMapper.load(SearchRestaurant.class, restaurantName)).thenReturn(searchRestaurant);

        // Act
        restaurantRepository.findItemsUnderRestaurant(null, restaurantName, null, null, 0, 10);

        // Assert
        verify(dynamoDBMapper, times(1)).load(SearchRestaurant.class, restaurantName);
    }

    @Test
    void sortResultsByField_ValidField_SortsSearchResults() {
        // Arrange
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(new RestaurantSearchResult("Restaurant 1", "Address 1", "Item 1", "4.5", "$$"));
        searchResults.add(new RestaurantSearchResult("Restaurant 2", "Address 2", "Item 3", "4.2", "$$$"));
        searchResults.add(new RestaurantSearchResult("Restaurant 3", "Address 3", "Item 2", "4.8", "$"));

        // Act
        List<SearchResult> sortedResults = restaurantRepository.sortResultsByField(searchResults, "itemName");

        // Assert
        verify(logger, never()).error(anyString(), any(Exception.class));
        //verify(logger, never()).warn(anyString(), any());
        verify(logger, never()).info(anyString());
        verify(logger, never()).debug(anyString());
        verify(logger, never()).trace(anyString());
        assertSame(searchResults.get(2), sortedResults.get(2)); // Item 1
        assertSame(searchResults.get(0), sortedResults.get(0)); // Item 2
        assertSame(searchResults.get(1), sortedResults.get(1)); // Item 3
    }

    @Test
    void saveRestaurant_ValidSearchRestaurant_SuccessfullySaved() {
        // Arrange
        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName("Restaurant 1");

        // Act
        SearchRestaurant savedRestaurant = restaurantRepository.saveRestaurant(searchRestaurant);

        // Assert
        assertNotNull(savedRestaurant);
        assertEquals("Restaurant 1", savedRestaurant.getRestaurantName());
        verify(dynamoDBMapper, times(1)).save(searchRestaurant);
    }

    @Test
    void sortResultsByField_ValidField_SortedResults() {
        // Arrange
        List<SearchResult> searchResults = new ArrayList<>();
        RestaurantSearchResult result1 = new RestaurantSearchResult();
        result1.setName("Restaurant 1");
        result1.setAddress("Address 1");
        result1.setItemName("Item 1");
        result1.setRatings("4.5");
        result1.setPrice("$10");

        RestaurantSearchResult result2 = new RestaurantSearchResult();
        result2.setName("Restaurant 2");
        result2.setAddress("Address 2");
        result2.setItemName("Item 2");
        result2.setRatings("4.2");
        result2.setPrice("$15");

        searchResults.add(result2);
        searchResults.add(result1);

        // Act
        List<SearchResult> sortedResults = restaurantRepository.sortResultsByField(searchResults, "itemName");

        // Assert
        assertEquals(2, sortedResults.size());
        assertEquals("Item 1", sortedResults.get(0).getItemName());
        assertEquals("Item 2", sortedResults.get(1).getItemName());
    }

    @Test
    public void findItemsUnderRestaurant_ReturnsSearchResults() {
        String restaurantName = "Restaurant A";
        String filter = "Pizza";
        String sort = "itemName";
        int page = 0;
        int size = 5;

        SearchRestaurant searchRestaurant = new SearchRestaurant();
        searchRestaurant.setRestaurantName(restaurantName);
        searchRestaurant.setAddress("123 Main St");

        Menu menu1 = new Menu();
        menu1.setItemName("Pizza");
        menu1.setRatings("4.5");
        menu1.setPrice("110.99");

        Menu menu2 = new Menu();
        menu2.setItemName("Naan");
        menu2.setRatings("3.8");
        menu2.setPrice("118.99");

        MenuList menuList = new MenuList();
        menuList.setItems(Arrays.asList(menu1, menu2));

        searchRestaurant.setMenuList(menuList);

        ReviewRequestItem reviewRequestItem1 = new ReviewRequestItem();
        reviewRequestItem1.setRestaurantName(restaurantName);
        reviewRequestItem1.setItemName("Pizza");

        ReviewRequestItem reviewRequestItem2 = new ReviewRequestItem();
        reviewRequestItem2.setRestaurantName(restaurantName);
        reviewRequestItem2.setItemName("Naan");

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setItems(Arrays.asList(reviewRequestItem1, reviewRequestItem2));

        ReviewResponseItem reviewResponseItem1 = new ReviewResponseItem();
        reviewResponseItem1.setRestaurantName(restaurantName);
        reviewResponseItem1.setItemName("Pizza");
        reviewResponseItem1.setRatings("4.7");

        ReviewResponseItem reviewResponseItem2 = new ReviewResponseItem();
        reviewResponseItem2.setRestaurantName(restaurantName);
        reviewResponseItem2.setItemName("Naan");
        reviewResponseItem2.setRatings("4.2");

        List<ReviewResponseItem> fetchedReviews = Arrays.asList(reviewResponseItem1, reviewResponseItem2);

        when(dynamoDBMapper.load(SearchRestaurant.class, restaurantName)).thenReturn(searchRestaurant);
        when(feignClient.fetchReviews(reviewRequest)).thenReturn(fetchedReviews);

        List<SearchResult> searchResults = restaurantRepository.findItemsUnderRestaurant("", restaurantName, filter, sort, page, size);

        assertEquals(1, searchResults.size(), "The number of search results should be 1");
        assertEquals("Pizza", searchResults.get(0).getItemName(), "The first search result should have the correct item name");
        assertEquals("4.7", searchResults.get(0).getRatings(), "The first search result should have the correct ratings");

        verify(dynamoDBMapper, times(1)).load(SearchRestaurant.class, restaurantName);
        verify(feignClient, times(1)).fetchReviews(reviewRequest);
    }

    @Test
    public void findItemsUnderRestaurant_WithInvalidRestaurantName_ReturnsEmptyList() {
        String restaurantName = "Invalid Restaurant";

        when(dynamoDBMapper.load(SearchRestaurant.class, restaurantName)).thenReturn(null);

        List<SearchResult> searchResults = restaurantRepository.findItemsUnderRestaurant("", restaurantName, null, null, 0, 10);

        assertEquals(0, searchResults.size(), "The search results should be empty");

        verify(dynamoDBMapper, times(1)).load(SearchRestaurant.class, restaurantName);
        verify(feignClient, never()).fetchReviews(any(ReviewRequest.class));
    }

}
