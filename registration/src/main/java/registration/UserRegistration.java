package registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import registration.model.AppUser;
import registration.repository.UserRepository;

@EnableEurekaClient
@SpringBootApplication
public class UserRegistration implements ApplicationRunner {

    @Autowired
    UserRepository userRepo; // Repository for accessing user data.

    @Autowired
    PasswordEncoder encoder; // Password encoder for encoding user passwords.

    public static void main(String[] args) {
        SpringApplication.run(UserRegistration.class, args); // Start the UserRegistration application.
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // This method is executed after the application context has been loaded.

        // Save sample user data to the user repository for testing purposes.
        // The saved users will be available in the application for authentication and authorization.

        // Save a customer user
        userRepo.saveUser(new AppUser(
                "customer1@email.com",
                "customer1",
                "123423456",
                encoder.encode("password1"),
                "CUSTOMER"
        ));

        // Save another customer user
        userRepo.saveUser(new AppUser(
                "customer2@email.com",
                "customer2",
                "144323456",
                encoder.encode("password2"),
                "CUSTOMER"
        ));

        // Save an admin user
        userRepo.saveUser(new AppUser(
                "admin1@email.com",
                "admin1",
                "23423423456",
                encoder.encode("password3"),
                "ADMIN"
        ));

        // Save another admin user
        userRepo.saveUser(new AppUser(
                "admin2@email.com",
                "admin2",
                "2342342434",
                encoder.encode("password4"),
                "ADMIN"
        ));
    }
}
