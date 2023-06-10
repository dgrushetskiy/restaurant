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

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConf.class);

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    public LoadData myTaskOne(RestaurantRepository restaurantRepository){
        return new LoadData(restaurantRepository);
    }

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


    @Bean
    public Job demoJob(){
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .build();
    }

    @Bean
    public Step stepOne(){
        return steps.get("stepOne")
                .tasklet(myTaskOne(restaurantRepository))
                .build();
    }

}
