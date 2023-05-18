package registration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import registration.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Customer saveCustomer(Customer customer) {
        dynamoDBMapper.save(customer);
        return customer;
    }

    public Customer getCustomerByEmail(String email) {
        return dynamoDBMapper.load(Customer.class, email);
    }


}