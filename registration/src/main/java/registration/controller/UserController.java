package registration.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import registration.model.AppUser;
import registration.model.LoginModel;
import registration.model.Response;
import registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registration.service.AppUserDetailsService;
import registration.util.JwtTokenUtil;

@RestController
@RequestMapping("/food/api/v1/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new appUser.
     *
     * @param appUser The AppUser object containing appUser details.
     * @return ResponseEntity indicating the result of the registration process.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser appUser) {
        AppUser existingAppUser = userRepository.getUserByEmail(appUser.getEmail());

        try {
            LOGGER.info("Registering user with email: {}", appUser.getEmail());

            if (existingAppUser != null) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            String encodedPassword = passwordEncoder.encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);

            userRepository.saveUser(appUser);
            LOGGER.info("User registered successfully with email: {}", appUser.getEmail());

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            LOGGER.error("Error occurred while registering appUser with email: {}", appUser.getEmail(), e);
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
    public ResponseEntity<?> loginUser(@RequestBody LoginModel loginObj)  {

        AppUser existingAppUser = userRepository.getUserByEmail(loginObj.getEmail());
        try {
            LOGGER.info("Logging in user with email: {}", loginObj.getEmail());

            if (existingAppUser == null) {
                LOGGER.warn("User with email {} not found", loginObj.getEmail());

                return ResponseEntity.badRequest().body("User not found");
            }

            if (!passwordEncoder.matches(loginObj.getPassword(), existingAppUser.getPassword())) {
                LOGGER.warn("Incorrect password for user with email: {}", loginObj.getEmail());

                return ResponseEntity.badRequest().body("Incorrect password");
            }
            LOGGER.info("AppUser logged in successfully with email: {}", loginObj.getEmail());

            //return ResponseEntity.ok("AppUser logged in successfully");
        }catch (Exception e) {
            LOGGER.error("Error occurred while logging in user with email: {}", loginObj.getEmail(), e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }

//        try {
//            AppUser existingAppUser = userRepository.getUserByEmail(loginObj.getEmail());
//
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginObj.getEmail(), loginObj.getPassword())
//            );
//            System.out.println("Authenticated successfully");
//        }
//        catch (BadCredentialsException e) {
//            return ResponseEntity.badRequest().body("Incorrect username or password");
//        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginObj.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new Response(jwt));


//        AppUser existingAppUser = userRepository.getUserByEmail(loginObj.getEmail());
//        try {
//            LOGGER.info("Logging in user with email: {}", loginObj.getEmail());
//
//            if (existingAppUser == null) {
//                LOGGER.warn("AppUser with email {} not found", loginObj.getEmail());
//
//                return ResponseEntity.badRequest().body("AppUser not found");
//            }
//
//            if (!passwordEncoder.matches(loginObj.getPassword(), existingAppUser.getPassword())) {
//                LOGGER.warn("Incorrect password for user with email: {}", loginObj.getEmail());
//
//                return ResponseEntity.badRequest().body("Incorrect password");
//            }
//            LOGGER.info("AppUser logged in successfully with email: {}", loginObj.getEmail());
//
//            return ResponseEntity.ok("AppUser logged in successfully");
//        }catch (Exception e) {
//            LOGGER.error("Error occurred while logging in user with email: {}", loginObj.getEmail(), e);
//            return ResponseEntity.status(500).body("Internal Server Error");
//        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testMethod () {
        return ResponseEntity.ok().body("Processed Successfully");
    }

    @GetMapping("/best")
    public ResponseEntity<?> bestMethod () {
        return ResponseEntity.ok().body("Processed Successfully");
    }
}