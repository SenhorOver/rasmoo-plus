package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.model.UserCredentials;
import com.client.ws.rasmooplus.services.AuthenticationService;
import com.client.ws.rasmooplus.services.TokenService;
import com.client.ws.rasmooplus.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    public TokenDto auth(LoginDto dto) {
        try {
            UserCredentials userCredentials = userDetailsService.loadUserByUsernameAndPass(dto.getUsername(), dto.getPassword());
            String token = tokenService.getToken(userCredentials.getId());
            return TokenDto.builder()
                    .token(token)
                    .type("Bearer")
                    .build();

        } catch (Exception e) {
            throw new BadRequestException("Erro ao formatar token - "+e.getMessage());
        }
    }
}
