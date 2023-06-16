package dataload.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataload.model.Restaurant;
import dataload.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class LoadData implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadData.class);

    private RestaurantRepository restaurantRepository;

    // Constructor-based dependency injection for the RestaurantRepository
    public LoadData(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // Method that gets executed when the tasklet is run
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LOGGER.info("LoadData start..");

        // ObjectMapper for JSON deserialization
        ObjectMapper mapper = new ObjectMapper();

        // TypeReference to preserve generic type information during deserialization
        TypeReference<List<Restaurant>> typeReference = new TypeReference<List<Restaurant>>(){};

        // Read the JSON data file as an InputStream
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");

        try {
            // Deserialize the JSON data into a list of Restaurant objects
            List<Restaurant> restaurants = mapper.readValue(inputStream, typeReference);

            // Save each restaurant to the repository
            restaurants.forEach(restaurant -> restaurantRepository.saveRestaurant(restaurant));

            LOGGER.info("Data Saved!");
        } catch (IOException e) {
            LOGGER.info("Unable to save data: " + e.getMessage());
        }

        LOGGER.info("LoadData done..");
        return RepeatStatus.FINISHED;
    }
}
