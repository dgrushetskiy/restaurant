package registration.controller;

import registration.model.Customer;
import registration.model.LoginModel;
import registration.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/food/api/v1/user")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     *
     * @param customer The Customer object containing user details.
     * @return ResponseEntity indicating the result of the registration process.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer existingUser = customerRepository.getCustomerByEmail(customer.getEmail());

        try {
            LOGGER.info("Registering user with email: {}", customer.getEmail());

            if (existingUser != null) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            String encodedPassword = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(encodedPassword);

            customerRepository.saveCustomer(customer);
            LOGGER.info("User registered successfully with email: {}", customer.getEmail());

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            LOGGER.error("Error occurred while registering user with email: {}", customer.getEmail(), e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    /**
     * Logs in a user.
     *
     * @param loginObj The LoginModel object containing login credentials.
     * @return ResponseEntity indicating the result of the login process.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginModel loginObj) {
        Customer existingUser = customerRepository.getCustomerByEmail(loginObj.getEmail());
        try {
            LOGGER.info("Logging in user with email: {}", loginObj.getEmail());

            if (existingUser == null) {
                LOGGER.warn("User with email {} not found", loginObj.getEmail());

                return ResponseEntity.badRequest().body("User not found");
            }

            if (!passwordEncoder.matches(loginObj.getPassword(), existingUser.getPassword())) {
                LOGGER.warn("Incorrect password for user with email: {}", loginObj.getEmail());

                return ResponseEntity.badRequest().body("Incorrect password");
            }
            LOGGER.info("User logged in successfully with email: {}", loginObj.getEmail());

            return ResponseEntity.ok("User logged in successfully");
        }catch (Exception e) {
            LOGGER.error("Error occurred while logging in user with email: {}", loginObj.getEmail(), e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}