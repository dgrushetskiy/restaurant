package updatePrice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import updatePrice.model.Menu;
import updatePrice.model.MenuList;
import updatePrice.model.Restaurant;
import updatePrice.repository.RestaurantRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/food/api/v1/admin")
public class UpdatePriceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePriceController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/update-price/menu/{restaurantName}")
    public ResponseEntity<String> updatePrice(
            @PathVariable String restaurantName,
            @RequestParam String menuItemName,
            @RequestParam String newPrice
    ) {

        try {
            LOGGER.info("Updating price for item: {} in restaurant: {}", menuItemName, restaurantName);

            Restaurant existingRestaurant = restaurantRepository.getRestaurantByRestaurantName(restaurantName);
            if (existingRestaurant == null) {
                LOGGER.warn("Restaurant not found: {}", restaurantName);
                return ResponseEntity.badRequest().body("Restaurant not found");
            }

            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            Matcher matcher = pattern.matcher(newPrice);
            if (!matcher.matches()) {
                LOGGER.warn("Non-numeric price: {} for item: {} in restaurant: {}", newPrice, menuItemName, restaurantName);
                return ResponseEntity.badRequest().body("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is non-numeric");
            }

            double price = Double.parseDouble(newPrice);
            if (price < 100 || price > 200) {
                LOGGER.warn("Invalid price range: {} for item: {} in restaurant: {}", newPrice, menuItemName, restaurantName);
                return ResponseEntity.badRequest().body("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is outside allowed range 100-200");
            }

            MenuList menuList = existingRestaurant.getMenuList();
            List<Menu> items = menuList.getItems();

            AtomicBoolean itemFound = new AtomicBoolean(false);
            items.replaceAll(menu -> {
                if (menu.getItemName().equals(menuItemName)) {
                    menu.setPrice(newPrice);
                    itemFound.set(true);
                }
                return menu;
            });

            if (!itemFound.get()) {
                LOGGER.warn("Menu item not found: {} in restaurant: {}", menuItemName, restaurantName);
                return ResponseEntity.badRequest().body("Menu item " + menuItemName + " under restaurant " + restaurantName + " is not found");
            }

            menuList.setItems(items);
            existingRestaurant.setMenuList(menuList);

            restaurantRepository.saveRestaurant(existingRestaurant);
            LOGGER.info("Price updated successfully for item: {} in restaurant: {}", menuItemName, restaurantName);
            return ResponseEntity.ok("Price updated successfully");
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating price for item: {} in restaurant: {}", menuItemName, restaurantName, e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
