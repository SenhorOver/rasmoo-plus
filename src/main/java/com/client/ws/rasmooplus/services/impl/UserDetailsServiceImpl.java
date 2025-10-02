package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.repositories.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDetailsRepository repository;

    @Override
    public UserCredentials loadUserByUsernameAndPass(String username, String pass) {
        Optional<UserCredentials> userCredentialsOpt = repository.findByUsername(username);

        if(userCredentialsOpt.isEmpty()) {
            throw new BadRequestException("User/Password Invalid");
        }

        UserCredentials userCredentials = userCredentialsOpt.get();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(pass, userCredentials.getPassword())) {
            return userCredentials;
        }
        throw new BadRequestException("User/Password Invalid");
    }
}
