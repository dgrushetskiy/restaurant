package reviews.model;

import java.util.List;

public class ReviewResponse {

    private List<ResponseItem> items;

    public List<ResponseItem> getItems() {
        return items;
    }

    public void setItems(List<ResponseItem> items) {
        this.items = items;
    }
}
