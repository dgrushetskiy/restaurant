package searchFood.handler;

import lombok.Data;
import searchFood.model.SearchResult;

import java.util.List;

@Data
public class SearchFoodQueryResult {
    private List<SearchResult> searchResults;
}
