package addRestaurant.model;

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
    private String ratings;

    @DynamoDBAttribute
    private String price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
