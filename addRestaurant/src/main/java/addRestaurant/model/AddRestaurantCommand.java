package addRestaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class AddRestaurantCommand {

    private String restaurantName; // Represents the name of the restaurant
    private String address; // Represents the address of the restaurant
    private MenuList menuList; // Represents the menu list of the restaurant

}
