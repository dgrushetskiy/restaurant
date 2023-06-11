package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import searchFood.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchRestaurantRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //restaurantRepository = new RestaurantRepository();
        //restaurantRepository.setDynamoDBMapper(dynamoDBMapper);
    }

//    @Test
//    void testSaveRestaurant() {
//        // Create a SearchRestaurant object
//        SearchRestaurant restaurant = new SearchRestaurant();
//        restaurant.setRestaurantName("Test SearchRestaurant");
//        restaurant.setAddress("123 Main St");
//
//        // Perform the save operation
//        SearchRestaurant savedRestaurant = restaurantRepository.saveRestaurant(restaurant);
//
//        // Verify that the save method of DynamoDBMapper is called once
//        verify(dynamoDBMapper, times(1)).save(restaurant);
//
//        // Verify that the returned restaurant is the same as the input restaurant
//        assertEquals(restaurant, savedRestaurant);
//    }

//    @Test
//    void testGetRestaurantByName() {
//        // Create a mock SearchRestaurant object
//        SearchRestaurant mockRestaurant = new SearchRestaurant();
//        mockRestaurant.setRestaurantName("Test SearchRestaurant");
//        mockRestaurant.setAddress("123 Main St");
//
//        // Specify the behavior of DynamoDBMapper's load method
//        when(dynamoDBMapper.load(SearchRestaurant.class, "Test SearchRestaurant")).thenReturn(mockRestaurant);
//
//        // Perform the get operation
//        SearchRestaurant retrievedRestaurant = restaurantRepository.getRestaurantByName("Test SearchRestaurant");
//
//        // Verify that the load method of DynamoDBMapper is called once
//        verify(dynamoDBMapper, times(1)).load(SearchRestaurant.class, "Test SearchRestaurant");
//
//        // Verify that the retrieved restaurant is the same as the mock restaurant
//        assertEquals(mockRestaurant, retrievedRestaurant);
//    }

    @Test
    void testFindItemsUnderRestaurant() {
        // Create a mock SearchRestaurant object with a menu
        SearchRestaurant mockSearchRestaurant = new SearchRestaurant();
        mockSearchRestaurant.setRestaurantName("Test SearchRestaurant");
        mockSearchRestaurant.setAddress("123 Main St");

        MenuList menuList = new MenuList();
        List<Menu> menuItems = new ArrayList<>();
        Menu menu1 = new Menu();
        menu1.setItemName("Item 1");
        menu1.setRatings("4.5");
        menu1.setPrice("$10.99");
        menuItems.add(menu1);
        menuList.setItems(menuItems);
        mockSearchRestaurant.setMenuList(menuList);

        // Specify the behavior of getRestaurantByName method
        //when(restaurantRepository.getRestaurantByName("Test SearchRestaurant")).thenReturn(mockSearchRestaurant);

        // Perform the findItemsUnderRestaurant operation
        List<SearchResult> searchResults = restaurantRepository.findItemsUnderRestaurant("restaurantname", "Test SearchRestaurant", null, null, 0, 10);

        // Verify that the getRestaurantByName method is called once
        //verify(restaurantRepository, times(1)).getRestaurantByName("Test SearchRestaurant");

        // Verify that the search results contain the expected menu item
        assertEquals(1, searchResults.size());
        RestaurantSearchResult searchResult = (RestaurantSearchResult) searchResults.get(0);
        assertEquals("Test SearchRestaurant", searchResult.getName());
        assertEquals("123 Main St", searchResult.getAddress());
        assertEquals("Item 1", searchResult.getItemName());
        assertEquals("4.5", searchResult.getRatings());
        assertEquals("$10.99", searchResult.getPrice());
    }

//    @Test
//    void testFindAllItemsByName() {
//        // Create a mock SearchRestaurant object with a menu
//        SearchRestaurant mockRestaurant1 = new SearchRestaurant();
//        mockRestaurant1.setRestaurantName("SearchRestaurant 1");
//        mockRestaurant1.setAddress("123 Main St");
//
//        MenuList menuList1 = new MenuList();
//        List<Menu> menuItems1 = new ArrayList<>();
//        Menu menu1 = new Menu();
//        menu1.setItemName("Item 1");
//        menu1.setRatings("4.5");
//        menu1.setPrice("$10.99");
//        menuItems1.add(menu1);
//        menuList1.setItems(menuItems1);
//        mockRestaurant1.setMenuList(menuList1);
//
//        SearchRestaurant mockRestaurant2 = new SearchRestaurant();
//        mockRestaurant2.setRestaurantName("SearchRestaurant 2");
//        mockRestaurant2.setAddress("456 Elm St");
//
//        MenuList menuList2 = new MenuList();
//        List<Menu> menuItems2 = new ArrayList<>();
//        Menu menu2 = new Menu();
//        menu2.setItemName("Item 2");
//        menu2.setRatings("3.8");
//        menu2.setPrice("$8.99");
//        menuItems2.add(menu2);
//        menuList2.setItems(menuItems2);
//        mockRestaurant2.setMenuList(menuList2);
//
//        // Create a list of mock restaurants
//        List<SearchRestaurant> mockRestaurantList = new ArrayList<>();
//        mockRestaurantList.add(mockRestaurant1);
//        mockRestaurantList.add(mockRestaurant2);
//
//        // Specify the behavior of DynamoDBMapper's scan method
//        when(dynamoDBMapper.scan(eq(SearchRestaurant.class), any(DynamoDBScanExpression.class))).thenReturn( mockRestaurantList);
//
//        // Perform the findAllItemsByName operation
//        List<SearchResult> searchResults = restaurantRepository.findAllItemsbyName("Item 1");
//
//        // Verify that the scan method of DynamoDBMapper is called once
//        verify(dynamoDBMapper, times(1)).scan(eq(SearchRestaurant.class), any(DynamoDBScanExpression.class));
//
//        // Verify that the search results contain the expected menu item
//        assertEquals(1, searchResults.size());
//        SearchResult searchResult = searchResults.get(0);
//        assertEquals("SearchRestaurant 1", searchResult.getRestaurantName());
//        assertEquals("123 Main St", searchResult.getAddress());
//        assertEquals("Item 1", searchResult.getItemName());
//        assertEquals("4.5", searchResult.getRatings());
//        assertEquals("$10.99", searchResult.getPrice());
//    }
}
