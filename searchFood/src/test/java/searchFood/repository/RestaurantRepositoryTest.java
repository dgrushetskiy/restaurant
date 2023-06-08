package searchFood.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import searchFood.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestaurantRepositoryTest {

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
//        // Create a Restaurant object
//        Restaurant restaurant = new Restaurant();
//        restaurant.setRestaurantName("Test Restaurant");
//        restaurant.setAddress("123 Main St");
//
//        // Perform the save operation
//        Restaurant savedRestaurant = restaurantRepository.saveRestaurant(restaurant);
//
//        // Verify that the save method of DynamoDBMapper is called once
//        verify(dynamoDBMapper, times(1)).save(restaurant);
//
//        // Verify that the returned restaurant is the same as the input restaurant
//        assertEquals(restaurant, savedRestaurant);
//    }

//    @Test
//    void testGetRestaurantByName() {
//        // Create a mock Restaurant object
//        Restaurant mockRestaurant = new Restaurant();
//        mockRestaurant.setRestaurantName("Test Restaurant");
//        mockRestaurant.setAddress("123 Main St");
//
//        // Specify the behavior of DynamoDBMapper's load method
//        when(dynamoDBMapper.load(Restaurant.class, "Test Restaurant")).thenReturn(mockRestaurant);
//
//        // Perform the get operation
//        Restaurant retrievedRestaurant = restaurantRepository.getRestaurantByName("Test Restaurant");
//
//        // Verify that the load method of DynamoDBMapper is called once
//        verify(dynamoDBMapper, times(1)).load(Restaurant.class, "Test Restaurant");
//
//        // Verify that the retrieved restaurant is the same as the mock restaurant
//        assertEquals(mockRestaurant, retrievedRestaurant);
//    }

    @Test
    void testFindItemsUnderRestaurant() {
        // Create a mock Restaurant object with a menu
        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setRestaurantName("Test Restaurant");
        mockRestaurant.setAddress("123 Main St");

        MenuList menuList = new MenuList();
        List<Menu> menuItems = new ArrayList<>();
        Menu menu1 = new Menu();
        menu1.setItemName("Item 1");
        menu1.setRatings("4.5");
        menu1.setPrice("$10.99");
        menuItems.add(menu1);
        menuList.setItems(menuItems);
        mockRestaurant.setMenuList(menuList);

        // Specify the behavior of getRestaurantByName method
        //when(restaurantRepository.getRestaurantByName("Test Restaurant")).thenReturn(mockRestaurant);

        // Perform the findItemsUnderRestaurant operation
        List<SearchResult> searchResults = restaurantRepository.findItemsUnderRestaurant("restaurantname", "Test Restaurant", null, null, 0, 10);

        // Verify that the getRestaurantByName method is called once
        //verify(restaurantRepository, times(1)).getRestaurantByName("Test Restaurant");

        // Verify that the search results contain the expected menu item
        assertEquals(1, searchResults.size());
        RestaurantSearchResult searchResult = (RestaurantSearchResult) searchResults.get(0);
        assertEquals("Test Restaurant", searchResult.getName());
        assertEquals("123 Main St", searchResult.getAddress());
        assertEquals("Item 1", searchResult.getItemName());
        assertEquals("4.5", searchResult.getRatings());
        assertEquals("$10.99", searchResult.getPrice());
    }

//    @Test
//    void testFindAllItemsByName() {
//        // Create a mock Restaurant object with a menu
//        Restaurant mockRestaurant1 = new Restaurant();
//        mockRestaurant1.setRestaurantName("Restaurant 1");
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
//        Restaurant mockRestaurant2 = new Restaurant();
//        mockRestaurant2.setRestaurantName("Restaurant 2");
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
//        List<Restaurant> mockRestaurantList = new ArrayList<>();
//        mockRestaurantList.add(mockRestaurant1);
//        mockRestaurantList.add(mockRestaurant2);
//
//        // Specify the behavior of DynamoDBMapper's scan method
//        when(dynamoDBMapper.scan(eq(Restaurant.class), any(DynamoDBScanExpression.class))).thenReturn( mockRestaurantList);
//
//        // Perform the findAllItemsByName operation
//        List<SearchResult> searchResults = restaurantRepository.findAllItemsbyName("Item 1");
//
//        // Verify that the scan method of DynamoDBMapper is called once
//        verify(dynamoDBMapper, times(1)).scan(eq(Restaurant.class), any(DynamoDBScanExpression.class));
//
//        // Verify that the search results contain the expected menu item
//        assertEquals(1, searchResults.size());
//        SearchResult searchResult = searchResults.get(0);
//        assertEquals("Restaurant 1", searchResult.getRestaurantName());
//        assertEquals("123 Main St", searchResult.getAddress());
//        assertEquals("Item 1", searchResult.getItemName());
//        assertEquals("4.5", searchResult.getRatings());
//        assertEquals("$10.99", searchResult.getPrice());
//    }
}
