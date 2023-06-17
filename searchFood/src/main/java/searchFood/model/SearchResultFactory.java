package searchFood.model;

/**
 * Factory class for creating search results based on the type.
 */
public class SearchResultFactory {

    /**
     * Creates a search result based on the given type.
     *
     * @param type The type of search result to create.
     * @return The created search result object.
     */
    public static SearchResult getSearchResult(String type){

        // Check the type and create the corresponding search result
        if (type.equalsIgnoreCase("SearchRestaurant")){
            return new RestaurantSearchResult();
        }

        // Return null if the type is not recognized
        return null;
    }
}
