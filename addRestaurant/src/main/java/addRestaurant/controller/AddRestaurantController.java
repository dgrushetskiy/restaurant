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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/food/api/v1/admin")
public class AddRestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/add-restaurant")
    public ResponseEntity<String> addRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant existingRestaurant = restaurantRepository.getRestaurantByName(restaurant.getRestaurantName());

        if (existingRestaurant != null) {
            return ResponseEntity.badRequest().body("Restaurant already exists");
        }

        MenuList menuList = restaurant.getMenuList();

        List<Menu> items = menuList.getItems();
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        for (Menu menu : items) {

            Matcher matcher = pattern.matcher(menu.getPrice());
            if (!matcher.matches()) {
                return ResponseEntity.badRequest().body("Price " + menu.getPrice() + " of item " + menu.getItemName() + " is non-numeric");
            }

            double price = Double.parseDouble(menu.getPrice());
            if (price < 100 || price > 200) {
                return ResponseEntity.badRequest().body("Price " + menu.getPrice() + " of item " + menu.getItemName() + " is outside allowed range 100-200");
            }

            matcher = pattern.matcher(menu.getRatings());
            if (!matcher.matches()) {
                return ResponseEntity.badRequest().body("Rating " + menu.getRatings() + " of item " + menu.getItemName() + " is non-numeric");
            }

            double rating = Double.parseDouble(menu.getRatings());
            if (rating < 1 || rating > 10) {
                return ResponseEntity.badRequest().body("Rating " + menu.getRatings() + " of item " + menu.getItemName() + " is outside allowed range 1-10");
            }
        }

        restaurantRepository.saveRestaurant(restaurant);

        return ResponseEntity.ok("Restaurant saved successfully");
    }
}
