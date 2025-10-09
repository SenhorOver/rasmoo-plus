package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private static final String USERNAME = "marcos@email.com";
    private static final String PASSWORD = "123";
    private static final Long USER_ID = 1L;
    private static final String TOKEN = "token";

    private UserCredentials userCredentials;
    private LoginDto loginDto;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {
        userCredentials = new UserCredentials(USER_ID, USERNAME, PASSWORD, new UserType(1L, "Aluno", "Aluno descrição"));
        loginDto = new LoginDto(USERNAME, PASSWORD);
        tokenDto = new TokenDto(TOKEN, "Bearer");
    }

    @Test
    void given_auth_when_credentialsIsValid_then_return_newTokenDto() {
        when(userDetailsService.loadUserByUsernameAndPass(USERNAME, PASSWORD)).thenReturn(userCredentials);
        when(tokenService.getToken(USER_ID)).thenReturn(TOKEN);

        Assertions.assertEquals(tokenDto, authenticationService.auth(loginDto));

        verify(userDetailsService, times(1)).loadUserByUsernameAndPass(USERNAME, PASSWORD);
        verify(tokenService, times(1)).getToken(USER_ID);
    }

    @Test
    void given_auth_when_credentialsIsInvalid_then_return_throwsBadRequestException() {
        when(userDetailsService.loadUserByUsernameAndPass(USERNAME, PASSWORD)).thenThrow(new BadRequestException("User Not Found"));

        Assertions.assertThrows(BadRequestException.class,() -> authenticationService.auth(loginDto));

        verify(userDetailsService, times(1)).loadUserByUsernameAndPass(USERNAME, PASSWORD);
        verify(tokenService, times(0)).getToken(USER_ID);
    }

}