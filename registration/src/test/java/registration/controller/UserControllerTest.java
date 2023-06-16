package registration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import registration.model.AppUser;
import registration.model.LoginModel;
import registration.model.Response;
import registration.repository.UserRepository;
import registration.service.AppUserDetailsService;
import registration.util.JwtTokenUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_WithNewUser_ReturnsSuccessResponse() {
        // Arrange
        AppUser appUser = createValidCustomer();
        when(userRepository.getUserByEmail(appUser.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(appUser.getPassword())).thenReturn("password");
        when(userRepository.saveUser(appUser)).thenReturn(appUser);

        // Act
        ResponseEntity<String> response = userController.registerUser(appUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository, times(1)).getUserByEmail(appUser.getEmail());
        verify(passwordEncoder, times(1)).encode(appUser.getPassword());
        verify(userRepository, times(1)).saveUser(appUser);
    }

    @Test
    void testRegisterUser_WithExistingUser_ReturnsBadRequestResponse() {
        // Arrange
        AppUser appUser = createValidCustomer();
        when(userRepository.getUserByEmail(appUser.getEmail())).thenReturn(appUser);

        // Act
        ResponseEntity<String> response = userController.registerUser(appUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
        verify(userRepository, times(1)).getUserByEmail(appUser.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveUser(appUser);
    }

    // Add more test cases to cover other scenarios

    private AppUser createValidCustomer() {
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        appUser.setPassword("password");
        appUser.setName("Test");
        appUser.setMobileNumber("2436342626");

        return appUser;
    }

    @Test
    void loginUser_ValidCredentials_ReturnsJwtToken() {
        // Arrange
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("test@example.com");
        loginModel.setPassword("password");

        AppUser existingAppUser = new AppUser();
        existingAppUser.setEmail("test@example.com");
        existingAppUser.setPassword(passwordEncoder.encode("password"));

        when(userRepository.getUserByEmail("test@example.com")).thenReturn(existingAppUser);
        when(passwordEncoder.matches("password", existingAppUser.getPassword())).thenReturn(true);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        String jwtToken = "jwtToken";
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(jwtToken);

        // Act
        ResponseEntity<?> response = userController.loginUser(loginModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new Response(jwtToken), response.getBody());

        // Verify the interactions with mocks
        verify(userRepository, times(1)).getUserByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("password", existingAppUser.getPassword());
        verify(userDetailsService, times(1)).loadUserByUsername("test@example.com");
        verify(jwtTokenUtil, times(1)).generateToken(userDetails);
    }

    @Test
    void testLoginUser_WithNonExistingUser_ReturnsBadRequestResponse() {
        // Arrange
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("nonexisting@example.com");
        loginModel.setPassword("password");
        when(userRepository.getUserByEmail(loginModel.getEmail())).thenReturn(null);

        // Act
        ResponseEntity<String> response = (ResponseEntity<String>) userController.loginUser(loginModel);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userRepository, times(1)).getUserByEmail(loginModel.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }


}
