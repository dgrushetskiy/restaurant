package addRestaurant.controller;

import addRestaurant.model.Menu;
import addRestaurant.model.MenuList;
import addRestaurant.model.Restaurant;
import addRestaurant.model.Command;
import addRestaurant.repository.RestaurantRepository;
import addRestaurant.service.CommandHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/food/api/v1/admin")
public class AddRestaurantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRestaurantController.class);

    @Autowired
    CommandHandler commandHandler;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final RabbitTemplate rabbitTemplate;

    public AddRestaurantController(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    /**
     * Adds a new restaurant with menu details.
     *
     * @param restaurantRequest The Restaurant object to be added.
     * @return ResponseEntity with success message if the restaurant is added successfully, or error message if validation or database error occurs.
     */
    @PostMapping("/add-restaurant")
    public ResponseEntity<String> addRestaurant(@RequestBody Command restaurantRequest) {
        try {
            // Check if the restaurant already exists
            Restaurant existingRestaurant = restaurantRepository.getRestaurantByName(restaurantRequest.getRestaurantName());

            LOGGER.info("Adding restaurant: {}", restaurantRequest.getRestaurantName());

            if (existingRestaurant != null) {
                LOGGER.warn("Restaurant already exists: {}", restaurantRequest.getRestaurantName());
                return ResponseEntity.badRequest().body("Restaurant already exists");
            }

            MenuList menuList = restaurantRequest.getMenuList();

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
//            Restaurant restaurant = new Restaurant();
//            restaurant.setRestaurantName(restaurantRequest.getRestaurantName());
//            restaurant.setAddress(restaurantRequest.getAddress());
//            restaurant.setMenuList(restaurantRequest.getMenuList());
//            restaurant.setCreatedAt(String.valueOf(LocalDateTime.now()));
//
//            restaurantRepository.saveRestaurant(restaurant);

            //rabbitTemplate.convertAndSend(exchange, routingJsonKey, restaurantRequest);
            //commandHandler.handleCommand(restaurantRequest);


            String orderJson = objectMapper.writeValueAsString(restaurantRequest);
            Message message = MessageBuilder
                    .withBody(orderJson.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            this.rabbitTemplate.convertAndSend("addrestaurant-command", message);

            LOGGER.info("Restaurant saved successfully: {}", restaurantRequest.getRestaurantName());

            return ResponseEntity.ok("Restaurant saved successfully");
        } catch (Exception e) {
            LOGGER.error("Error occurred while adding restaurant: {}", restaurantRequest.getRestaurantName(), e);
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
