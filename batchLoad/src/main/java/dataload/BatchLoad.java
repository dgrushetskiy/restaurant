package dataload;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataload.model.Restaurant;
import dataload.repository.RestaurantRepository;
import dataload.util.FilePathUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@EnableBatchProcessing
@SpringBootApplication
public class BatchLoad implements CommandLineRunner {

//    @Autowired
//    JobLauncher jobLauncher;
//
//    @Autowired
//    Job job;


    public static void main(String[] args) {
        SpringApplication.run(BatchLoad.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //*****DO NOT DELETE******

/*        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);*/

        //*****WORKING - DO NOT DELETE******
/*        String clsPath =  new ClassPathResource("data.json").getPath();
        String data = FilePathUtils.readFileToString(clsPath, Restaurant.class);
        System.out.println(data);*/

//*****WORKING - DO NOT DELETE******
//        ObjectMapper mapper = new ObjectMapper();
//        TypeReference<List<Restaurant>> typeReference = new TypeReference<List<Restaurant>>(){};
//        InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");
//        try {
//            List<Restaurant> restaurants = mapper.readValue(inputStream,typeReference);
//
//            //restaurants.stream().forEach(restaurant -> System.out.println(restaurant.getRestaurantName()));
//            restaurants.stream().forEach(restaurant -> restaurantRepository.saveRestaurant(restaurant));
//
//            System.out.println("Data Saved!");
//        } catch (IOException e){
//            System.out.println("Unable to save data: " + e.getMessage());
//        }
    }
}