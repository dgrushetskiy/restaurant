//package addRestaurant.config;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.verify;
//
//class DynamoDBConfigTest {
//
//    @Mock
//    private AmazonDynamoDB amazonDynamoDB;
//
//    private DynamoDBConfig dynamoDBConfig;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        dynamoDBConfig = new DynamoDBConfig();
//    }
//
//    @Test
//    void dynamoDBMapper_ShouldBuildAmazonDynamoDBAndCreateDynamoDBMapper() {
//        // Arrange
//        dynamoDBConfig.setDynamodbEndpoint("http://localhost:8000");
//        dynamoDBConfig.setAwsRegion("us-east-1");
//        dynamoDBConfig.setDynamodbAccessKey("accessKey");
//        dynamoDBConfig.setDynamodbSecretKey("secretKey");
//
//        // Act
//        dynamoDBConfig.dynamoDBMapper();
//
//        // Assert
//        verify(amazonDynamoDB).withEndpointConfiguration(
//                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"));
//        verify(amazonDynamoDB).withCredentials(any(AWSStaticCredentialsProvider.class));
//        verify(amazonDynamoDB).build();
//    }
//}
