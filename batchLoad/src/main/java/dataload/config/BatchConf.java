package dataload.config;

import dataload.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConf {

    // Logger for logging purposes
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConf.class);

    // Repository for accessing restaurant data
    @Autowired
    RestaurantRepository restaurantRepository;

    // Bean definition for the task that loads data
    @Autowired
    public LoadData myTaskOne(RestaurantRepository restaurantRepository) {
        return new LoadData(restaurantRepository);
    }

    // Job builder factory for creating batch jobs
    @Autowired
    private JobBuilderFactory jobs;

    // Step builder factory for creating batch steps
    @Autowired
    private StepBuilderFactory steps;

    // Bean definition for the main batch job
    @Bean
    public Job demoJob() {
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer()) // Incrementer for generating unique job run IDs
                .start(stepOne()) // Starting point of the job: Step "stepOne"
                .build();
    }

    // Bean definition for the first step in the batch job
    @Bean
    public Step stepOne() {
        return steps.get("stepOne")
                .tasklet(myTaskOne(restaurantRepository)) // Tasklet that executes the data loading task
                .build();
    }

}
