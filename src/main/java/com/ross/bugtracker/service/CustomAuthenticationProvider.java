package com.ross.bugtracker.service;

import com.ross.bugtracker.model.Role;
import com.ross.bugtracker.model.UserDetails;
import com.ross.bugtracker.repository.UserDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsRepository userDetailsRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserDetailsRepository userDetailsRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserDetails> user = userDetailsRepository.findById(userName);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getEncryptedPassword())) {
            log.info("User {} successfully logged in", authentication);
            return new UsernamePasswordAuthenticationToken(
                    userName, password, Arrays.asList(new SimpleGrantedAuthority(user.get().getRole().name()))
            );
        }
        log.warn("User tried unsuccessfully to log in with data: {}", authentication);
        throw new AuthenticationCredentialsNotFoundException("");
    }

    public boolean register(String userName, String fullname, Role role, String password) {
        if (!userDetailsRepository.findById(userName).isPresent()) {
            String encryptedPassword = passwordEncoder.encode(password);

            userDetailsRepository.save(UserDetails.builder().userName(userName).fullName(fullname).encryptedPassword(encryptedPassword).role(role).build());
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
