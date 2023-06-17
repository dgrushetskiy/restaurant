package searchFood.model;

import lombok.Data;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public abstract class SearchResult {
    private String name;
    private String address;
    private String itemName;
    private String Ratings;
    private String price;
}
