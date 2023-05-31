package registration.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import registration.model.AppUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppUserRepositoryTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepository();
        userRepository.setDynamoDBMapper(dynamoDBMapper);
    }

    @Test
    void testSaveCustomer() {
        // Create a AppUser object
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        appUser.setName("John Doe");
        appUser.setMobileNumber("123456789");
        appUser.setPassword("password");

        // Perform the save operation
        AppUser savedAppUser = userRepository.saveUser(appUser);

        // Verify that the save method of DynamoDBMapper is called once
        verify(dynamoDBMapper, times(1)).save(appUser);

        // Verify that the returned appUser is the same as the input appUser
        assertEquals(appUser, savedAppUser);
    }

    @Test
    void testGetCustomerByEmail() {
        // Create a mock AppUser object
        AppUser mockAppUser = new AppUser();
        mockAppUser.setEmail("test@example.com");
        mockAppUser.setName("John Doe");
        mockAppUser.setMobileNumber("123456789");
        mockAppUser.setPassword("password");

        // Specify the behavior of DynamoDBMapper's load method
        when(dynamoDBMapper.load(AppUser.class, "test@example.com")).thenReturn(mockAppUser);

        // Perform the get operation
        AppUser retrievedAppUser = userRepository.getUserByEmail("test@example.com");

        // Verify that the load method of DynamoDBMapper is called once
        verify(dynamoDBMapper, times(1)).load(AppUser.class, "test@example.com");

        // Verify that the retrieved customer is the same as the mock customer
        assertEquals(mockAppUser, retrievedAppUser);
    }
}
