package addRestaurant.controller;

import addRestaurant.model.Menu;
import addRestaurant.model.MenuList;
import addRestaurant.model.Restaurant;
import addRestaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/food/api/v1/admin")
public class AddRestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRestaurantController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Adds a new restaurant with menu details.
     *
     * @param restaurant The Restaurant object to be added.
     * @return ResponseEntity with success message if the restaurant is added successfully, or error message if validation or database error occurs.
     */
    @PostMapping("/add-restaurant")
    public ResponseEntity<String> addRestaurant(@RequestBody Restaurant restaurant) {
        try {
            // Check if the restaurant already exists
            Restaurant existingRestaurant = restaurantRepository.getRestaurantByName(restaurant.getRestaurantName());

            LOGGER.info("Adding restaurant: {}", restaurant.getRestaurantName());

            if (existingRestaurant != null) {
                LOGGER.warn("Restaurant already exists: {}", restaurant.getRestaurantName());
                return ResponseEntity.badRequest().body("Restaurant already exists");
            }

            MenuList menuList = restaurant.getMenuList();

            List<Menu> items = menuList.getItems();
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");

            // Validate the menu items
            for (Menu menu : items) {
                LOGGER.info("Validating item: {}", menu.getItemName());
                Matcher matcher = pattern.matcher(menu.getPrice());
                // Validate the price
                if (!matcher.matches()) {
                    LOGGER.warn("Non-numeric price: {} for item: {}", menu.getPrice(), menu.getItemName());
                    return ResponseEntity.badRequest().body("Price " + menu.getPrice() + " of item " + menu.getItemName() + " is non-numeric");
                }

                if (!isValidValue(String.valueOf(menu.getItemName()))){
                    LOGGER.warn("Invalid item name: {}", menu.getItemName());
                    return ResponseEntity.badRequest().body("Item name " + menu.getItemName() + " is invalid");
                }
                double price = Double.parseDouble(menu.getPrice());
                if (price < 100 || price > 200) {
                    LOGGER.warn("Invalid price range: {} for item: {}", menu.getPrice(), menu.getItemName());
                    return ResponseEntity.badRequest().body("Price " + menu.getPrice() + " of item " + menu.getItemName() + " is outside allowed range 100-200");
                }

                // Validate the ratings
                matcher = pattern.matcher(menu.getRatings());
                if (!matcher.matches()) {
                    LOGGER.warn("Non-numeric rating: {} for item: {}", menu.getRatings(), menu.getItemName());
                    return ResponseEntity.badRequest().body("Rating " + menu.getRatings() + " of item " + menu.getItemName() + " is non-numeric");
                }

                double rating = Double.parseDouble(menu.getRatings());
                if (rating < 1 || rating > 10) {
                    LOGGER.warn("Invalid rating range: {} for item: {}", menu.getRatings(), menu.getItemName());
                    return ResponseEntity.badRequest().body("Rating " + menu.getRatings() + " of item " + menu.getItemName() + " is outside allowed range 1-10");
                }
            }

            // Save the restaurant to the database
            restaurantRepository.saveRestaurant(restaurant);
            LOGGER.info("Restaurant saved successfully: {}", restaurant.getRestaurantName());

            return ResponseEntity.ok("Restaurant saved successfully");
        } catch (Exception e) {
            LOGGER.error("Error occurred while adding restaurant: {}", restaurant.getRestaurantName(), e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    public static boolean isValidValue(String value) {
        for (Menu.ItemName itemName : Menu.ItemName.values()) {
            if (itemName.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
