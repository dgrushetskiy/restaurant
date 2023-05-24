package registration.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoginModelTest {

    @Test
    void testGettersAndSetters() {
        // Create a LoginModel object
        LoginModel loginModel = new LoginModel();

        // Set values using setters
        loginModel.setEmail("test@example.com");
        loginModel.setPassword("password");

        // Verify values using getters
        assertEquals("test@example.com", loginModel.getEmail());
        assertEquals("password", loginModel.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        // Create a LoginModel object using the no-args constructor
        LoginModel loginModel = new LoginModel();

        // Verify that all fields are initialized to null
        assertNull(loginModel.getEmail());
        assertNull(loginModel.getPassword());
    }
}
