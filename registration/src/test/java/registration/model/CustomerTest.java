package registration.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerTest {

    @Test
    void testGettersAndSetters() {
        // Create a Customer object
        Customer customer = new Customer();

        // Set values using setters
        customer.setEmail("test@example.com");
        customer.setName("John Doe");
        customer.setMobileNumber("1234567890");
        customer.setPassword("password");

        // Verify values using getters
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("John Doe", customer.getName());
        assertEquals("1234567890", customer.getMobileNumber());
        assertEquals("password", customer.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a Customer object using the no-args constructor
        Customer customer = new Customer();

        // Verify that all fields are initialized to null
        assertNull(customer.getEmail());
        assertNull(customer.getName());
        assertNull(customer.getMobileNumber());
        assertNull(customer.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        // Create a Customer object using the all-args constructor
        Customer customer = new Customer("test@example.com", "John Doe", "1234567890", "password");

        // Verify the values set in the constructor
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("John Doe", customer.getName());
        assertEquals("1234567890", customer.getMobileNumber());
        assertEquals("password", customer.getPassword());
    }
}
