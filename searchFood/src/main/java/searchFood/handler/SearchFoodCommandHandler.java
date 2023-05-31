package searchFood.handler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SearchFoodCommandHandler {

    @RabbitListener(queues = "searchfood-commands")
    public void handleSearchCommand(SearchFoodCommand searchCommand) {
        // Handle the search command here
        // Execute the search operation based on the command parameters
        // Update the data source accordingly
    }
}
