package searchFood.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class MenuList {

    @DynamoDBAttribute
    private List<Menu> items;

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }
}
