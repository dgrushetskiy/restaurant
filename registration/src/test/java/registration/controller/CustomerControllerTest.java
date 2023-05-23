package registration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import registration.model.Customer;
import registration.model.LoginModel;
import registration.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_WithNewUser_ReturnsSuccessResponse() {
        // Arrange
        Customer customer = createValidCustomer();
        when(customerRepository.getCustomerByEmail(customer.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("password");
        when(customerRepository.saveCustomer(customer)).thenReturn(customer);

        // Act
        ResponseEntity<String> response = customerController.registerUser(customer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(customerRepository, times(1)).getCustomerByEmail(customer.getEmail());
        verify(passwordEncoder, times(1)).encode(customer.getPassword());
        verify(customerRepository, times(1)).saveCustomer(customer);
    }

    @Test
    void testRegisterUser_WithExistingUser_ReturnsBadRequestResponse() {
        // Arrange
        Customer customer = createValidCustomer();
        when(customerRepository.getCustomerByEmail(customer.getEmail())).thenReturn(customer);

        // Act
        ResponseEntity<String> response = customerController.registerUser(customer);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
        verify(customerRepository, times(1)).getCustomerByEmail(customer.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(customerRepository, never()).saveCustomer(customer);
    }

    // Add more test cases to cover other scenarios

    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password");
        customer.setName("Test");
        customer.setMobileNumber("2436342626");

        return customer;
    }

    @Test
    void testLoginUser_WithValidCredentials_ReturnsSuccessResponse() {
        // Arrange
        Customer existingUser = createValidCustomer();
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(existingUser.getEmail());
        loginModel.setPassword(existingUser.getPassword());
        when(customerRepository.getCustomerByEmail(loginModel.getEmail())).thenReturn(existingUser);
        when(passwordEncoder.matches(loginModel.getPassword(), existingUser.getPassword())).thenReturn(true);

        // Act
        ResponseEntity<String> response = customerController.loginUser(loginModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully", response.getBody());
        verify(customerRepository, times(1)).getCustomerByEmail(loginModel.getEmail());
        verify(passwordEncoder, times(1)).matches(loginModel.getPassword(), existingUser.getPassword());
    }

    @Test
    void testLoginUser_WithNonExistingUser_ReturnsBadRequestResponse() {
        // Arrange
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("nonexisting@example.com");
        loginModel.setPassword("password");
        when(customerRepository.getCustomerByEmail(loginModel.getEmail())).thenReturn(null);

        // Act
        ResponseEntity<String> response = customerController.loginUser(loginModel);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(customerRepository, times(1)).getCustomerByEmail(loginModel.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    // Add more test cases to cover other scenarios
}
