package registration.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenUtil {
    private String SECRET_KEY = "secret"; // Secret key used for JWT signing and validation.

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extract the username from the JWT token's claims.
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract the expiration date from the JWT token's claims.
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extract all claims from the JWT token.
        return claimsResolver.apply(claims); // Apply the claims resolver function to obtain a specific claim value.
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); // Parse the JWT token and extract all claims.
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check if the JWT token is expired based on the expiration date.
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // Create a map of claims to be included in the JWT token.
        return createToken(claims, userDetails.getUsername()); // Create a JWT token with the specified claims and subject.
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Set the claims for the JWT token.
                .setSubject(subject) // Set the subject (username) for the JWT token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issued date/time for the JWT token.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set the expiration date/time for the JWT token.
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sign the JWT token using the specified algorithm and secret key.
                .compact(); // Compact the JWT token into its final string representation.
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extract the username from the JWT token.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Validate the JWT token by comparing the username and expiration.
    }
}
