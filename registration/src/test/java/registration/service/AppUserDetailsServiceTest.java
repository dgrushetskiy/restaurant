package registration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import registration.model.AppUser;
import registration.repository.UserRepository;
import registration.service.AppUserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository; // Mocked repository for user data.

    @InjectMocks
    private AppUserDetailsService appUserDetailsService; // Service under test.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks before each test.
    }

    @Test
    void loadUserByUsername_ValidUser_ReturnsUserDetails() {
        // Arrange
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        appUser.setPassword("password");
        appUser.setRole("ROLE_USER");
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(appUser);

        // Act
        UserDetails userDetails = appUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(User.withUsername("test@example.com").password("password").authorities("ROLE_USER").build().getAuthorities().iterator().next()));
    }

    @Test
    void loadUserByUsername_InvalidUser_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> appUserDetailsService.loadUserByUsername("test@example.com"));
    }
}
