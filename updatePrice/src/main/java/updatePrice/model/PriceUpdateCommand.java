package updatePrice.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdateCommand {
    private String restaurantName;
    private String address;
    private MenuList menuList;
    private String createdAt;
    private String updatedAt;
}
