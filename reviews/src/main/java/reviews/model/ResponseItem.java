package reviews.model;

import lombok.Data;

@Data //Lombok annotation that automatically generates getter and setter methods, toString, equals, and hashCode methods
public class ResponseItem {

    private String restaurantName;
    private String itemName;
    private String ratings;
}
