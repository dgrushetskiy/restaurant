package searchFood.model;

import lombok.Data;

import java.util.List;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
public class ReviewRequest {
    private List<ReviewRequestItem> items;
}
