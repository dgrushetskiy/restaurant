package addRestaurant.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@DynamoDBTable(tableName = "restaurant") // Specifies the DynamoDB table name for the class
public class Restaurant {

    @DynamoDBHashKey // Marks the field as the hash key of the DynamoDB table
    @DynamoDBAttribute // Specifies that the field is mapped to a DynamoDB attribute
    private String restaurantName; // Represents the name of the restaurant

    @DynamoDBAttribute // Specifies that the field is mapped to a DynamoDB attribute
    private String address; // Represents the address of the restaurant

    @DynamoDBAttribute // Specifies that the field is mapped to a DynamoDB attribute
    private MenuList menuList; // Represents the menu list of the restaurant

    @DynamoDBAttribute // Specifies that the field is mapped to a DynamoDB attribute
    private String createdAt; // Represents the creation timestamp of the restaurant

    @DynamoDBAttribute // Specifies that the field is mapped to a DynamoDB attribute
    private String updatedAt; // Represents the last update timestamp of the restaurant

}
