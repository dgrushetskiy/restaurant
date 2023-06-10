package dataload.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import dataload.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
//@EnableBatchProcessing
public class BatchConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConf.class);

//    @Value("${amazon.dynamodb.endpoint}")
//    private String dynamodbEndpoint;
//
//    @Value("${amazon.aws.region}")
//    private String awsRegion;
//
//    @Value("${amazon.aws.accesskey}")
//    private String dynamodbAccessKey;
//
//    @Value("${amazon.aws.secretkey}")
//    private String dynamodbSecretKey;

//    @Bean
//    public AmazonDynamoDB buildAmazonDynamoDB() {
//        try{
//            return AmazonDynamoDBClientBuilder
//                    .standard()
//                    .withEndpointConfiguration(
//                            new AwsClientBuilder.EndpointConfiguration(dynamodbEndpoint,awsRegion))
//                    .withCredentials(new AWSStaticCredentialsProvider(
//                            new BasicAWSCredentials(dynamodbAccessKey,dynamodbSecretKey)))
//                    .build();
//        } catch (Exception e) {
//            LOGGER.error("Error occurred while building AmazonDynamoDB client", e);
//            throw e;
//        }
//    }

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    public MyTaskOne myTaskOne(RestaurantRepository restaurantRepository){
        return new MyTaskOne(restaurantRepository);
    }

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

//    @Autowired
//    private JobBuilder jobbuilder;

//    @Autowired
//    private StepBuilder stepbuilder;


//    @Bean
//    public Job demoJob(JobRepository jobRepository, Step step) {
//        return new JobBuilder("demoJob", jobRepository)
//                .start(step)
//                .build();
//    }

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



//    @Bean
//    public Tasklet myTasklet() {
//        return new MyTaskOne();
//    }
//
//    @Bean
//    public Step stepOne(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("myStep", jobRepository)
//                .tasklet(myTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
//                .build();
//    }
}
