package searchFood.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantSearchResultTest {

    @Test
    void gettersAndSetters_AllFieldsMatch_ReturnsCorrectValues() {
        // Arrange
        String name = "Restaurant 1";
        String address = "123 Main Street";
        String itemName = "Item 1";
        String ratings = "4.5";
        String price = "$$";

        // Act
        RestaurantSearchResult result = new RestaurantSearchResult();
        result.setName(name);
        result.setAddress(address);
        result.setItemName(itemName);
        result.setRatings(ratings);
        result.setPrice(price);

        // Assert
        assertEquals(name, result.getName());
        assertEquals(address, result.getAddress());
        assertEquals(itemName, result.getItemName());
        assertEquals(ratings, result.getRatings());
        assertEquals(price, result.getPrice());
    }

    @Test
    void constructor_AllFieldsMatch_ReturnsCorrectValues() {
        // Arrange
        String name = "Restaurant 1";
        String address = "123 Main Street";
        String itemName = "Item 1";
        String ratings = "4.5";
        String price = "$$";

        // Act
        RestaurantSearchResult result = new RestaurantSearchResult(name, address, itemName, ratings, price);

        // Assert
        assertEquals(name, result.getName());
        assertEquals(address, result.getAddress());
        assertEquals(itemName, result.getItemName());
        assertEquals(ratings, result.getRatings());
        assertEquals(price, result.getPrice());
    }



}
