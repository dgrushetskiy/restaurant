package registration.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import registration.repository.UserRepository;
import registration.service.AppUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AppUserDetailsService appUserDetailsService; // Service for retrieving user details.

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // Utility class for JWT token operations.

    @Autowired
    private UserRepository userRepo; // Repository for accessing user data.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String jwt = null;

        if (header != null && header.startsWith("Bearer ")) {
            System.out.println("Header contains bearer token");
            jwt = header.substring(7); // Extract the JWT from the Authorization header.
            username = jwtTokenUtil.extractUsername(jwt); // Extract the username from the JWT.
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = appUserDetailsService.loadUserByUsername(username); // Load user details based on the username.

            if (jwtTokenUtil.validateToken(jwt, userDetails)) { // Validate the JWT token.
                System.out.println("JWT Token is Valid.");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // Create an authentication token.
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set additional details for authentication.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); // Set the authentication in the SecurityContextHolder.
            }
        }
        chain.doFilter(request, response); // Continue with the filter chain.
    }

}
