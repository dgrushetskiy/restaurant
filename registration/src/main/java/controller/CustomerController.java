package controller;

import model.Customer;
import repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food/api/v1/user")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer existingUser = customerRepository.getCustomerByEmail(customer.getEmail());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        customerRepository.saveCustomer(customer);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Customer customer) {
        Customer existingUser = customerRepository.getCustomerByEmail(customer.getEmail());
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (!passwordEncoder.matches(customer.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        return ResponseEntity.ok("User logged in successfully");
    }
}