package updatePrice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import updatePrice.model.Menu;
import updatePrice.model.MenuList;
import updatePrice.model.Restaurant;
import updatePrice.repository.RestaurantRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/food/api/v1/admin")
public class UpdatePriceController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/update-price/menu/{restaurantName}")
    public ResponseEntity<String> updatePrice(
            @PathVariable String restaurantName,
            @RequestParam String menuItemName,
            @RequestParam String newPrice
    ) {
        Restaurant existingRestaurant = restaurantRepository.getRestaurantByName(restaurantName);
        if (existingRestaurant == null) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(newPrice);
        if (!matcher.matches()) {
            return ResponseEntity.badRequest().body("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is non-numeric");
        }

        double price = Double.parseDouble(newPrice);
        if (price < 100 || price > 200) {
            return ResponseEntity.badRequest().body("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is outside allowed range 100-200");
        }

        MenuList menuList = existingRestaurant.getMenulist();
        List<Menu> items = menuList.getItems();

        items.replaceAll(menu -> {
            if (menu.getItemName().equals(menuItemName)) {
                menu.setPrice(newPrice);
            }
            return menu;
        });
        menuList.setItems(items);
        existingRestaurant.setMenulist(menuList);

        restaurantRepository.saveRestaurant(existingRestaurant);

        return ResponseEntity.ok("Price updated successfully");
    }

}
