package searchFood.model;

public class SearchResultFactory {

    public static SearchResult getSearchResult(String type){

        if (type.equalsIgnoreCase("Restaurant")){
            return new RestaurantSearchResult();
        }

        return null;
    }
}
