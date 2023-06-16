package addRestaurant.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MenuListTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        List<Menu> items = new ArrayList<>();
        items.add(new Menu("Pizza", "4.5", "12.99"));
        items.add(new Menu("Burger", "4.0", "9.99"));

        // Act
        MenuList menuList = new MenuList();
        menuList.setItems(items);

        // Assert
        assertNotNull(menuList.getItems());
        assertEquals(items, menuList.getItems());
    }

    @Test
    void constructor_AllArgsConstructor() {
        // Create sample menu items
        Menu menu1 = new Menu("Burger", "4.5", "10.99");
        Menu menu2 = new Menu("Pizza", "4.2", "8.99");
        Menu menu3 = new Menu("French Fries", "3.7", "6.99");

        // Create a list of menu items
        List<Menu> items = Arrays.asList(menu1, menu2, menu3);

        // Create an instance using the all-args constructor
        MenuList menuList = new MenuList(items);

        // Assert that the instance is not null
        assertNotNull(menuList);

        // Assert that the list of items is set correctly
        assertEquals(items, menuList.getItems());
    }
}
