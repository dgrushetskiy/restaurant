package apigateway.service;

import apigateway.model.AppUser;
import apigateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repoUsr;

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {

        // Retrieve the AppUser entity from the UserRepository based on the provided useremail
        AppUser appUser = repoUsr.getUserByEmail(useremail);

        if (appUser == null) {
            // If the user is not found, throw a UsernameNotFoundException
            throw new UsernameNotFoundException(useremail);
        }

        // Create a UserDetails object using the retrieved AppUser entity
        UserDetails usr = User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .authorities(appUser.getRole())
                .build();

        return usr;
    }
}
