package searchFood.model;

import lombok.Data;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class ReviewRequestItem {

    private String restaurantName;
    private String itemName;
}
