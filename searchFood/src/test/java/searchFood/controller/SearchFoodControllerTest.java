package searchFood.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import searchFood.model.SearchResult;
import searchFood.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SearchFoodControllerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private SearchFoodController searchFoodController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFood_WithValidRestaurantName_ReturnsSearchResults() {
        // Arrange
        String criteria = "restaurantname";
        String criteriaValue = "Restaurant1";
        List<SearchResult> searchResults = createSearchResults();
        when(restaurantRepository.findItemsUnderRestaurant(criteriaValue)).thenReturn(searchResults);

        // Act
        ResponseEntity<Object> response = searchFoodController.searchFood(criteria, criteriaValue);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(searchResults, response.getBody());
        verify(restaurantRepository, times(1)).findItemsUnderRestaurant(criteriaValue);
    }

    @Test
    void testSearchFood_WithValidMenuItem_ReturnsSearchResults() {
        // Arrange
        String criteria = "menuitem";
        String criteriaValue = "Pizza";
        List<SearchResult> searchResults = createSearchResults();
        when(restaurantRepository.findAllItemsbyName(criteriaValue)).thenReturn(searchResults);

        // Act
        ResponseEntity<Object> response = searchFoodController.searchFood(criteria, criteriaValue);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(searchResults, response.getBody());
        verify(restaurantRepository, times(1)).findAllItemsbyName(criteriaValue);
    }

    @Test
    void testSearchFood_WithInvalidCriteria_ReturnsBadRequestResponse() {
        // Arrange
        String criteria = "invalid";
        String criteriaValue = "value";

        // Act
        ResponseEntity<Object> response = searchFoodController.searchFood(criteria, criteriaValue);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid search criteria", response.getBody());
        verify(restaurantRepository, never()).findItemsUnderRestaurant(anyString());
        verify(restaurantRepository, never()).findAllItemsbyName(anyString());
    }

    // Add more test cases to cover other scenarios

    private List<SearchResult> createSearchResults() {
        List<SearchResult> searchResults = new ArrayList<>();
        // Add search results

        return searchResults;
    }
}
