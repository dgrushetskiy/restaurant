package updatePrice.model;

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

    @DynamoDBHashKey
    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String address;

    @DynamoDBAttribute
    private MenuList menulist;

//    @DynamoDBTypeConverted(converter = MenuConverter.class)
//    public List<Menu> getItems() {
//        return items;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


//    public void setItems(List<Menu> items) {
//        this.items = items;
//    }
}