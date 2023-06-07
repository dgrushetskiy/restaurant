package searchFood.model;

public class RestaurantSearchResult extends SearchResult{
    private String name;
    private String address;
    private String itemName;
    private String Ratings;
    private String price;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getRatings() {
        return Ratings;
    }

    @Override
    public void setRatings(String ratings) {
        Ratings = ratings;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }
}
