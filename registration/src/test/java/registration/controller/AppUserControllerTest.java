package registration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import registration.model.AppUser;
import registration.model.LoginModel;
import registration.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppUserControllerTest {

    @Mock
    private UserRepository userRepository;

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
        assertEquals("AppUser registered successfully", response.getBody());
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
    void testLoginUser_WithValidCredentials_ReturnsSuccessResponse() {
        // Arrange
        AppUser existingAppUser = createValidCustomer();
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(existingAppUser.getEmail());
        loginModel.setPassword(existingAppUser.getPassword());
        when(userRepository.getUserByEmail(loginModel.getEmail())).thenReturn(existingAppUser);
        when(passwordEncoder.matches(loginModel.getPassword(), existingAppUser.getPassword())).thenReturn(true);

        // Act
        ResponseEntity<String> response = (ResponseEntity<String>) userController.loginUser(loginModel);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AppUser logged in successfully", response.getBody());
        verify(userRepository, times(1)).getUserByEmail(loginModel.getEmail());
        verify(passwordEncoder, times(1)).matches(loginModel.getPassword(), existingAppUser.getPassword());
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
        assertEquals("AppUser not found", response.getBody());
        verify(userRepository, times(1)).getUserByEmail(loginModel.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    // Add more test cases to cover other scenarios
}
