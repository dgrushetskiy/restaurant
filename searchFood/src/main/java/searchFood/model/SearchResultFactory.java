package searchFood.model;

public class SearchResultFactory {

    public static SearchResult getSearchResult(String type){

        if (type.equalsIgnoreCase("SearchRestaurant")){
            return new RestaurantSearchResult();
        }

        return null;
    }
}
