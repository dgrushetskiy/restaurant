package addRestaurant.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
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
    private String restaurantName;

    @DynamoDBAttribute
    private String address;

    @DynamoDBAttribute
    private MenuList menuList;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public MenuList getMenuList() {
        return menuList;
    }

    public void setMenuList(MenuList menuList) {
        this.menuList = menuList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}