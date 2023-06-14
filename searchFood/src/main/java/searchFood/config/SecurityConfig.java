//package searchFood.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import searchFood.repository.UserRepository;
//import searchFood.service.AppUserDetailsService;
//import searchFood.util.JwtTokenFilter;
//
//import javax.servlet.http.HttpServletResponse;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  {
//
//    @Autowired
//    private JwtTokenFilter jwtTokenFilter;
//
//    @Autowired
//    private AppUserDetailsService appUserDetailsService;
//
//    @Autowired
//    UserRepository repoUsr;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // Enable CORS and disable CSRF
//        http = http.cors().and().csrf().disable();
//
//        // Set session management to stateless
//        http = http
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and();
//
//        // Set unauthorized requests exception handler
//        http = http
//                .exceptionHandling()
//                .authenticationEntryPoint(
//                        (request, response, ex) -> {
//                            response.sendError(
//                                    HttpServletResponse.SC_UNAUTHORIZED,
//                                    ex.getMessage()
//                            );
//                        }
//                )
//                .and();
//
//        // Set permissions on endpoints
//        http.authorizeRequests(authorize -> authorize
//                // Our public endpoints
//                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/proxy/**").permitAll()
//
//                // Our private endpoints
//                .antMatchers(HttpMethod.GET, "/food/api/v1/user/restaurantname/**").hasAnyAuthority( "CUSTOMER")
//                .antMatchers(HttpMethod.GET, "/food/api/v1/user/menuitem/**").hasAuthority("CUSTOMER")
//                .anyRequest().authenticated()
//        );
//
//        // Add JWT token filter
//        http.addFilterBefore(
//                jwtTokenFilter,
//                UsernamePasswordAuthenticationFilter.class
//        );
//
//        // Build the SecurityFilterChain
//        return http.build();
//    }
//
//}