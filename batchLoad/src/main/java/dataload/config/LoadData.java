package dataload.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataload.model.Restaurant;
import dataload.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
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


    //@Autowired
    private RestaurantRepository restaurantRepository;

    public LoadData(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
    {
        System.out.println("LoadData start..");
        //RestaurantRepository restaurantRepository =  new RestaurantRepository();
        //Restaurant restaurant = new Restaurant();
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Restaurant>> typeReference = new TypeReference<List<Restaurant>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");
        try {
            List<Restaurant> restaurants = mapper.readValue(inputStream,typeReference);

            restaurants.stream().forEach(restaurant -> restaurantRepository.saveRestaurant(restaurant));

            System.out.println("Data Saved!");
        } catch (IOException e){
            System.out.println("Unable to save data: " + e.getMessage());
        }

        System.out.println("LoadData done..");
        return RepeatStatus.FINISHED;
    }
}