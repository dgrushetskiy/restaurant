package searchFood.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Lombok annotation to automatically generate getters, setters, equals, hashCode, and toString methods
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@DynamoDBTable(tableName = "searchrestaurant")
public class SearchRestaurant {

    @DynamoDBHashKey
    @DynamoDBAttribute
    private String restaurantName;

    @DynamoDBAttribute
    private String address;

    @DynamoDBAttribute
    private MenuList menuList;

    @DynamoDBAttribute
    private String createdAt;

    @DynamoDBAttribute
    private String updatedAt;

}