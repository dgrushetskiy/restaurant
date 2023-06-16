package registration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import registration.repository.UserRepository;
import registration.service.AppUserDetailsService;
import registration.util.JwtTokenFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    UserRepository repoUsr;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests(authorize -> authorize
                // Our public endpoints
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/proxy/**").permitAll()
                .antMatchers(HttpMethod.POST, "/food/api/v1/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/food/api/v1/user/register").permitAll()

                // Our private endpoints
                .antMatchers(HttpMethod.GET, "/food/api/v1/user/test").hasAnyAuthority("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/food/api/v1/user/best").hasAuthority("ADMIN")
                .anyRequest().authenticated()
        );

        // Add JWT token filter
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        // Build the SecurityFilterChain
        return http.build();
    }

}