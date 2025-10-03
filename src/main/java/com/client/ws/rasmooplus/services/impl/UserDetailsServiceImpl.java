package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repositories.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repositories.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${webservices.rasplus.redis.recoverycode.timeout}")
    private String recoveryCodeTimeout;

    @Autowired
    private UserDetailsRepository repository;

    @Autowired
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @Autowired
    private MailIntegration mailIntegration;

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

    @Override
    public void sendRecoveryCode(String email) {

        UserRecoveryCode userRecoveryCode;
        String code = String.format("%04d", new Random().nextInt(10000));

        Optional<UserRecoveryCode> userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);

        if(userRecoveryCodeOpt.isEmpty()) {
            Optional<UserCredentials> user = repository.findByUsername(email);
            if(user.isEmpty()) {
                throw new NotFoundException("User not found");
            }

            userRecoveryCode = new UserRecoveryCode();
            userRecoveryCode.setEmail(email);
        } else {
            userRecoveryCode = userRecoveryCodeOpt.get();
        }
        userRecoveryCode.setCode(code);
        userRecoveryCode.setCreationDate(LocalDateTime.now());

        userRecoveryCodeRepository.save(userRecoveryCode);
        mailIntegration.send(email, "Código de recuperação de conta: "+code, "Código de recuperação de conta");
    }

    @Override
    public boolean recoveryCodeInValid(String recoveryCode, String email) {
        Optional<UserRecoveryCode> userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);

        if(userRecoveryCodeOpt.isEmpty()) {
            throw new NotFoundException("Uusário não encontrado");
        }

        UserRecoveryCode userRecoveryCode = userRecoveryCodeOpt.get();

        LocalDateTime timeout = userRecoveryCode.getCreationDate().plusMinutes(Long.parseLong(recoveryCodeTimeout));
        LocalDateTime now = LocalDateTime.now();

        return recoveryCode.equals(userRecoveryCode.getCode()) && now.isBefore(timeout);
    }
}
