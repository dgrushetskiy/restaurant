package gateway.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import gateway.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Saves a appUser to the DynamoDB table.
     *
     * @param appUser The AppUser object to be saved.
     * @return The saved appUser.
     */
    public AppUser saveUser(AppUser appUser) {
        dynamoDBMapper.save(appUser);
        return appUser;
    }

    /**
     * Retrieves a customer by email from the DynamoDB table.
     *
     * @param email The email of the customer to retrieve.
     * @return The retrieved customer or null if not found.
     */
    public AppUser getUserByEmail(String email) {
        return dynamoDBMapper.load(AppUser.class, email);
    }

    /**
     * Returns the DynamoDBMapper instance used by the repository.
     *
     * @return The DynamoDBMapper instance.
     */
    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    /**
     * Sets the DynamoDBMapper instance to be used by the repository.
     *
     * @param dynamoDBMapper The DynamoDBMapper instance.
     */
    public void setDynamoDBMapper(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }
}