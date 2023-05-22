package searchFood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchFood.model.Menu;
import searchFood.model.MenuList;
import searchFood.model.Restaurant;
import searchFood.model.SearchResult;
import searchFood.repository.RestaurantRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/food/api/v1/user")
public class SearchFoodController {

    @Autowired
    private RestaurantRepository restaurantRepository;


    @GetMapping("/{criteria}/{criteriaValue}")
    public ResponseEntity<Object> searchFood(
            @PathVariable String criteria,
            @PathVariable String criteriaValue
    ) {
        List<SearchResult> myList;
        if (criteria.equalsIgnoreCase("restaurantname")) {
            myList = restaurantRepository.findItemsUnderRestaurant(criteriaValue);
        } else if (criteria.equalsIgnoreCase("menuitem")) {
            myList = restaurantRepository.findAllItemsbyName(criteriaValue);
        } else {
            return ResponseEntity.badRequest().body("Invalid search criteria");
        }

        return ResponseEntity.ok(myList);
    }


//    @PostMapping("/update-price/menu/{restaurantName}")
//    public ResponseEntity<String> updatePrice(
//            @PathVariable String restaurantName,
//            @RequestParam String menuItemName,
//            @RequestParam String newPrice
//    ) {
//        Restaurant existingRestaurant = restaurantRepository.getRestaurantByName(restaurantName);
//        if (existingRestaurant == null) {
//            return ResponseEntity.badRequest().body("Restaurant not found");
//        }
//
//        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
//        Matcher matcher = pattern.matcher(newPrice);
//        if (!matcher.matches()) {
//            return ResponseEntity.badRequest().body("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is non-numeric");
//        }
//
//        double price = Double.parseDouble(newPrice);
//        if (price < 100 || price > 200) {
//            return ResponseEntity.badRequest().body("Price " + newPrice + " of item " + menuItemName + " under restaurant " + restaurantName + " is outside allowed range 100-200");
//        }
//
//        MenuList menuList = existingRestaurant.getMenulist();
//        List<Menu> items = menuList.getItems();
//
//        AtomicBoolean itemFound = new AtomicBoolean(false);
//        items.replaceAll(menu -> {
//            if (menu.getItemName().equals(menuItemName)) {
//                menu.setPrice(newPrice);
//                itemFound.set(true);
//            }
//            return menu;
//        });
//
//        if (!itemFound.get()) {
//            return ResponseEntity.badRequest().body("Menu item " + menuItemName + " under restaurant " + restaurantName + " is not found");
//        }
//
//        menuList.setItems(items);
//        existingRestaurant.setMenulist(menuList);
//
//        restaurantRepository.saveRestaurant(existingRestaurant);
//
//        return ResponseEntity.ok("Price updated successfully");
//    }

}
