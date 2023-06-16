package dataload.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "restaurant")
public class Restaurant {

    // DynamoDBHashKey annotation to mark the restaurantName field as the hash key
    @DynamoDBHashKey
    @DynamoDBAttribute
    private String restaurantName;

    // DynamoDBAttribute annotation to mark the address field for persistence
    @DynamoDBAttribute
    private String address;

    // A field representing a collection of menu items associated with the restaurant
    @DynamoDBAttribute
    private MenuList menuList;

    // DynamoDBAttribute annotation to mark the createdAt field for persistence
    @DynamoDBAttribute
    private String createdAt;

    // DynamoDBAttribute annotation to mark the updatedAt field for persistence
    @DynamoDBAttribute
    private String updatedAt;
}
