package addrestaurant.model;

public class Menu {
    private String itemName;
    private String Ratings;
    private String price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRatings() {
        return Ratings;
    }

    public void setRatings(String ratings) {
        Ratings = ratings;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
