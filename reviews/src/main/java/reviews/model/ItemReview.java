package reviews.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "review")
public class ItemReview {

    @DynamoDBHashKey
    @DynamoDBAttribute
    private String reviewId; // Unique identifier for the review

    @DynamoDBAttribute
    private String restaurantName; // Name of the restaurant associated with the review

    @DynamoDBAttribute
    private String itemName; // Name of the item being reviewed

    @DynamoDBAttribute
    private String ratings; // Ratings given to the item

    @DynamoDBAttribute
    private String comment; // Comment or feedback provided by the reviewer

}
