package registration.util;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import registration.util.JwtTokenUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenUtilTest {

    private static final String SECRET_KEY = "secret";

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void extractUsername_ValidToken_ReturnsUsername() {
        // Arrange
        String token = generateTokenWithUsername("test@example.com");

        // Act
        String username = jwtTokenUtil.extractUsername(token);

        // Assert
        assertEquals("test@example.com", username);
    }

    @Test
    void extractClaim_ValidToken_ReturnsClaimValue() {
        // Arrange
        String token = generateTokenWithClaim("userId", "123");

        // Act
        String claimValue = jwtTokenUtil.extractClaim(token, claims -> claims.get("userId", String.class));

        // Assert
        assertEquals("123", claimValue);
    }

    @Test
    void generateToken_CreatesTokenWithCorrectClaims() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Act
        String token = jwtTokenUtil.generateToken(userDetails);

        // Assert
        Jws<Claims> parsedToken = parseToken(token);
        assertEquals("test@example.com", parsedToken.getBody().getSubject());
        assertNotNull(parsedToken.getBody().getIssuedAt());
        assertNotNull(parsedToken.getBody().getExpiration());
    }

    @Test
    void validateToken_ValidTokenAndUserDetails_ReturnsTrue() {
        // Arrange
        String token = generateTokenWithUsername("test@example.com");
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Act
        boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    // Helper method to generate a token with a specific username
    private String generateTokenWithUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return jwtTokenUtil.createToken(claims, username);
    }

    // Helper method to generate a token with a specific expiration date
    private String generateTokenWithExpiration(Date expirationDate) {
        Map<String, Object> claims = new HashMap<>();
        return jwtTokenUtil.createToken(claims, "test@example.com");
    }

    // Helper method to generate a token with a specific claim
    private String generateTokenWithClaim(String claimName, Object claimValue) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(claimName, claimValue);
        return jwtTokenUtil.createToken(claims, "test@example.com");
    }

    // Helper method to parse a token
    private Jws<Claims> parseToken(String token) {
        JwtParser parser = Jwts.parser().setSigningKey(SECRET_KEY);
        return parser.parseClaimsJws(token);
    }
}
