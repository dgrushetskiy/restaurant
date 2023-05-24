package registration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import registration.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Saves a customer to the DynamoDB table.
     *
     * @param customer The Customer object to be saved.
     * @return The saved customer.
     */
    public Customer saveCustomer(Customer customer) {
        dynamoDBMapper.save(customer);
        return customer;
    }

    /**
     * Retrieves a customer by email from the DynamoDB table.
     *
     * @param email The email of the customer to retrieve.
     * @return The retrieved customer or null if not found.
     */
    public Customer getCustomerByEmail(String email) {
        return dynamoDBMapper.load(Customer.class, email);
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