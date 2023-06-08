package searchFood.model;

import java.util.List;

public class ReviewRequest {
    private List<ReviewRequestItem> items;

    public List<ReviewRequestItem> getItems() {
        return items;
    }

    public void setItems(List<ReviewRequestItem> items) {
        this.items = items;
    }
}
