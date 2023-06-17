package searchFood.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class PriceUpdateCommand {
    private String restaurantName;
    private String address;
    private MenuList menuList;
    private String createdAt;
    private String updatedAt;
}
