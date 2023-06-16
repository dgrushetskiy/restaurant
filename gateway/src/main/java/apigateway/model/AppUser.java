package apigateway.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "user") // Specifies the name of the DynamoDB table
public class AppUser {

    @DynamoDBHashKey // Marks the field as the hash key for the DynamoDB table
    @DynamoDBAttribute // Indicates that this field is an attribute of the DynamoDB table
    private String email; // Represents the email attribute in the DynamoDB table

    @DynamoDBAttribute
    private String name; // Represents the name attribute in the DynamoDB table

    @DynamoDBAttribute
    private String mobileNumber; // Represents the mobileNumber attribute in the DynamoDB table

    @DynamoDBAttribute
    private String password; // Represents the password attribute in the DynamoDB table

    @DynamoDBAttribute
    private String role; // Represents the role attribute in the DynamoDB table

}
