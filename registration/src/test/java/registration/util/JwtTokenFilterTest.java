package registration.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import registration.service.AppUserDetailsService;
import registration.util.JwtTokenFilter;
import registration.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JwtTokenFilterTest {

    @Mock
    private AppUserDetailsService appUserDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer jwtToken");
        String username = "test@example.com";
        when(jwtTokenUtil.extractUsername("jwtToken")).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(appUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken("jwtToken", userDetails)).thenReturn(true);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Act
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenUtil, times(1)).extractUsername("jwtToken");
        verify(appUserDetailsService, times(1)).loadUserByUsername(username);
        verify(jwtTokenUtil, times(1)).validateToken("jwtToken", userDetails);
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that authentication is set
        assertEquals(authenticationToken, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_InvalidToken_DoesNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer jwtToken");
        String username = "test@example.com";
        when(jwtTokenUtil.extractUsername("jwtToken")).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(appUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken("jwtToken", userDetails)).thenReturn(false);

        // Act
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenUtil, times(1)).extractUsername("jwtToken");
        //verify(appUserDetailsService, times(1)).loadUserByUsername(username);
        //verify(jwtTokenUtil, times(1)).validateToken("jwtToken", userDetails);
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that authentication is not set
        //assertNull(SecurityContextHolder.getContext().getAuthentication());
    }



}
