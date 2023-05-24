package searchFood.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        String itemName = "Pizza";
        String ratings = "4.5";
        String price = "12.99";

        // Act
        Menu menu = new Menu();
        menu.setItemName(itemName);
        menu.setRatings(ratings);
        menu.setPrice(price);

        // Assert
        assertEquals(itemName, menu.getItemName());
        assertEquals(ratings, menu.getRatings());
        assertEquals(price, menu.getPrice());
    }

    @Test
    void constructor_AllArgsConstructor() {
        // Create sample data
        String itemName = "Sample Item";
        String ratings = "4.5";
        String price = "10.99";

        // Create an instance using the all-args constructor
        Menu menu = new Menu(itemName, ratings, price);

        // Assert that the instance variables are set correctly
        assertEquals(itemName, menu.getItemName());
        assertEquals(ratings, menu.getRatings());
        assertEquals(price, menu.getPrice());
    }
}
