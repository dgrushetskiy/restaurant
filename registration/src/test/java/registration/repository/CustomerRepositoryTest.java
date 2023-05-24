package registration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import registration.model.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRepository = new CustomerRepository();
        customerRepository.setDynamoDBMapper(dynamoDBMapper);
    }

    @Test
    void testSaveCustomer() {
        // Create a Customer object
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setName("John Doe");
        customer.setMobileNumber("123456789");
        customer.setPassword("password");

        // Perform the save operation
        Customer savedCustomer = customerRepository.saveCustomer(customer);

        // Verify that the save method of DynamoDBMapper is called once
        verify(dynamoDBMapper, times(1)).save(customer);

        // Verify that the returned customer is the same as the input customer
        assertEquals(customer, savedCustomer);
    }

    @Test
    void testGetCustomerByEmail() {
        // Create a mock Customer object
        Customer mockCustomer = new Customer();
        mockCustomer.setEmail("test@example.com");
        mockCustomer.setName("John Doe");
        mockCustomer.setMobileNumber("123456789");
        mockCustomer.setPassword("password");

        // Specify the behavior of DynamoDBMapper's load method
        when(dynamoDBMapper.load(Customer.class, "test@example.com")).thenReturn(mockCustomer);

        // Perform the get operation
        Customer retrievedCustomer = customerRepository.getCustomerByEmail("test@example.com");

        // Verify that the load method of DynamoDBMapper is called once
        verify(dynamoDBMapper, times(1)).load(Customer.class, "test@example.com");

        // Verify that the retrieved customer is the same as the mock customer
        assertEquals(mockCustomer, retrievedCustomer);
    }
}
