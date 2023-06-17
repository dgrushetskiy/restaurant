package reviews.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBConfig.class);

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamodbEndpoint;

    @Value("${amazon.aws.region}")
    private String awsRegion;

    @Value("${amazon.aws.accesskey}")
    private String dynamodbAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String dynamodbSecretKey;


    /**
     * Creates a bean for the DynamoDBMapper.
     *
     * @return The DynamoDBMapper object.
     */
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }

    /**
     * Builds and configures the AmazonDynamoDB client.
     *
     * @return The configured AmazonDynamoDB client.
     */
    public AmazonDynamoDB buildAmazonDynamoDB() {
        try {
            return AmazonDynamoDBClientBuilder
                    .standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(dynamodbEndpoint, awsRegion))
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(dynamodbAccessKey, dynamodbSecretKey)))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error occurred while building AmazonDynamoDB client", e);
            throw e;
        }
    }
}