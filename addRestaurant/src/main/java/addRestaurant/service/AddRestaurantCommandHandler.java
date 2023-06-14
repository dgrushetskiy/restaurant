//package addRestaurant.service;
//
//import addRestaurant.model.AddRestaurantCommand;
//import addRestaurant.model.Restaurant;
//import addRestaurant.repository.RestaurantRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class AddRestaurantCommandHandler {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AddRestaurantCommandHandler.class);
//
//    @Autowired
//    RestaurantRepository restaurantRepository;
//
//   // private final AmqpTemplate amqpTemplate;
//
//   // @Autowired
//    //public AddRestaurantCommandHandler(AmqpTemplate amqpTemplate) {
//    //    this.amqpTemplate = amqpTemplate;
//    //}
//
////    @Bean
////    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
////        return new Jackson2JsonMessageConverter();
////    }
////
////    @RabbitListener(queues = "addrestaurant-command")
////    public void handleCommand(AddRestaurantCommand restaurantRequest) {
////        LOGGER.info("Message received in queue addrestaurant-command");
////        Restaurant restaurant = new Restaurant();
////        restaurant.setRestaurantName(restaurantRequest.getRestaurantName());
////        restaurant.setAddress(restaurantRequest.getAddress());
////        restaurant.setMenuList(restaurantRequest.getMenuList());
////        restaurant.setCreatedAt(String.valueOf(LocalDateTime.now()));
////
////        restaurantRepository.saveRestaurant(restaurant);
////    }
//}
