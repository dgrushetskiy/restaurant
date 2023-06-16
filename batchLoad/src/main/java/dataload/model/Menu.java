package dataload.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class Menu {

    // Enum representing possible item names
    public enum ItemName {
        Naan("Naan"),
        Pizza("Pizza"),
        Burger("Burger"),
        Fries("French Fries");

        private final String value;

        // Enum constructor
        ItemName(String value) {
            this.value = value;
        }

        // Getter for the enum value
        public String getValue() {
            return value;
        }
    }

    // DynamoDBAttribute annotation to mark the itemName field for persistence
    @DynamoDBAttribute
    private String itemName;

    // DynamoDBAttribute annotation to mark the ratings field for persistence
    @DynamoDBAttribute
    private String ratings;

    // DynamoDBAttribute annotation to mark the price field for persistence
    @DynamoDBAttribute
    private String price;

}
