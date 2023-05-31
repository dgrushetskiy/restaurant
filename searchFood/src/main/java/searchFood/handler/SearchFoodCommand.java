package searchFood.handler;

import lombok.Data;

@Data
public class SearchFoodCommand {
    private String criteria;
    private String criteriaValue;
}
