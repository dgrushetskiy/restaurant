package registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import registration.model.AppUser;
import registration.repository.UserRepository;

@SpringBootApplication
public class UserRegistration  implements ApplicationRunner {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder ;

    public static void main(String[] args) {
        SpringApplication.run(UserRegistration.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        userRepo.saveUser(new AppUser("customer1@email.com","customer1", "123423456", encoder.encode("password1"),"CUSTOMER" ));
        userRepo.saveUser(new AppUser("customer2@email.com","customer2", "144323456", encoder.encode("password2"),"CUSTOMER" ));
        userRepo.saveUser(new AppUser("admin1@email.com","admin1", "23423423456", encoder.encode("password3"),"ADMIN" ));
        userRepo.saveUser(new AppUser("admin2@email.com","admin2", "2342342434", encoder.encode("password4"),"ADMIN" ));

    }
}
