package reviews.model;

import lombok.Data;

import java.util.List;

@Data //Lombok annotation that automatically generates getter and setter methods, toString, equals, and hashCode methods
public class ReviewResponse {

    private List<ResponseItem> items;
}
