package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import searchFood.model.*;
import searchFood.util.ReviewsFeignClient;

import java.util.ArrayList;
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
        //restaurantRepository.LOGGER = logger;
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
        //verify(logger, times(1)).info("Saved searchRestaurant: Restaurant 1");
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
        //verify(logger, times(1)).info("Finding items under searchRestaurant: Restaurant 1");
    }


//    @Test
//    void findAllItemsbyName_ValidItemName_CallsDynamoDBMapperScan() {
//        // Arrange
//        String itemName = "Item 1";
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        List<SearchRestaurant> searchRestaurantList = new ArrayList<>();
//        when(dynamoDBMapper.scan(SearchRestaurant.class, scanExpression)).thenReturn(searchRestaurantList);
//
//        // Act
//        restaurantRepository.findAllItemsbyName(null, itemName, null, null, 0, 10);
//
//        // Assert
//        verify(dynamoDBMapper, times(1)).scan(SearchRestaurant.class, scanExpression);
//        verify(logger, times(1)).info("Finding items by name: Item 1");
//    }

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

//    @Test
//    void findAllItemsbyName_ValidParameters_ReturnsResults() {
//        // Arrange
//        String itemName = "Pizza";
//        String filter = "Italian";
//        String sort = "ratings";
//        int page = 0;
//        int size = 10;
//
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        List<SearchRestaurant> searchRestaurantList = new ArrayList<>();
//
//        SearchRestaurant searchRestaurant = new SearchRestaurant();
//        searchRestaurant.setRestaurantName("Restaurant 1");
//        searchRestaurant.setAddress("Address 1");
//        Menu menu = new Menu();
//        menu.setItemName(itemName);
//        menu.setRatings("4.5");
//        menu.setPrice("$10");
//        searchRestaurant.getMenuList().getItems().add(menu);
//        searchRestaurantList.add(searchRestaurant);
//
//        when(dynamoDBMapper.scan(SearchRestaurant.class, scanExpression)).thenReturn(any());
//
//        // Act
//        List<SearchResult> results = restaurantRepository.findAllItemsbyName(null, itemName, filter, sort, page, size);
//
//        // Assert
//        assertEquals(0, results.size());
////        assertEquals("Restaurant 1", results.get(0).getName());
////        assertEquals("Address 1", results.get(0).getAddress());
////        assertEquals("Pizza", results.get(0).getItemName());
////        assertEquals("4.5", results.get(0).getRatings());
////        assertEquals("$10", results.get(0).getPrice());
//    }

}
