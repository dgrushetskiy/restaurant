package updatePrice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@DynamoDBDocument // Indicates that this class is mapped to a DynamoDB document
public class Menu {

    public enum ItemName {
        Naan("Naan"),
        Pizza("Pizza"),
        Burger("Burger"),
        Fries("French Fries");

        private final String value;

        ItemName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @DynamoDBAttribute
    private String itemName;

    @DynamoDBAttribute
    private String Ratings;

    @DynamoDBAttribute
    private String price;

}
