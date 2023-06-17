package registration.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AppUserTest {

    @Test
    void testGettersAndSetters() {
        // Create a AppUser object
        AppUser appUser = new AppUser();

        // Set values using setters
        appUser.setEmail("test@example.com");
        appUser.setName("John Doe");
        appUser.setMobileNumber("1234567890");
        appUser.setPassword("password");

        // Verify values using getters
        assertEquals("test@example.com", appUser.getEmail());
        assertEquals("John Doe", appUser.getName());
        assertEquals("1234567890", appUser.getMobileNumber());
        assertEquals("password", appUser.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a AppUser object using the no-args constructor
        AppUser appUser = new AppUser();

        // Verify that all fields are initialized to null
        assertNull(appUser.getEmail());
        assertNull(appUser.getName());
        assertNull(appUser.getMobileNumber());
        assertNull(appUser.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        // Create a AppUser object using the all-args constructor
        AppUser appUser = new AppUser("test@example.com", "John Doe", "1234567890", "password", "admin");

        // Verify the values set in the constructor
        assertEquals("test@example.com", appUser.getEmail());
        assertEquals("John Doe", appUser.getName());
        assertEquals("1234567890", appUser.getMobileNumber());
        assertEquals("password", appUser.getPassword());
    }
}
