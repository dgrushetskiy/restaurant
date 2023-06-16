package searchFood.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Data;

import java.util.List;
@Data
public abstract class SearchResult {
    private String name;
    private String address;
    private String itemName;
    private String Ratings;
    private String price;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getItemName() {
//        return itemName;
//    }
//
//    public void setItemName(String itemName) {
//        this.itemName = itemName;
//    }
//
//    public String getRatings() {
//        return Ratings;
//    }
//
//    public void setRatings(String ratings) {
//        Ratings = ratings;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
}
