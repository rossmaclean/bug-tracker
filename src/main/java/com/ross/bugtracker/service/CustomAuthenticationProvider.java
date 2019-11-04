package com.ross.bugtracker.service;

import com.ross.bugtracker.model.Role;
import com.ross.bugtracker.model.UserCredentials;
import com.ross.bugtracker.repository.UserCredentialRepository;
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
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserCredentials> user = userCredentialRepository.findById(userName);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getEncryptedPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userName, password, Arrays.asList(new SimpleGrantedAuthority(user.get().getRole().name()))
            );
        }
        throw new AuthenticationCredentialsNotFoundException("");
    }

    public boolean register(String userName, String fullane, String password) {
        if (!userCredentialRepository.findById(userName).isPresent()) {
            String encryptedPassword = passwordEncoder.encode(password);

            userCredentialRepository.save(UserCredentials.builder().userName(userName).fullName(fullane).encryptedPassword(encryptedPassword).role(Role.DEFAULT).build());
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
