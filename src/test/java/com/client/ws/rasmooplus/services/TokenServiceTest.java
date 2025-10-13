package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.services.impl.TokenServiceImpl;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {


    @InjectMocks
    private TokenServiceImpl tokenService;

    private static final Long USER_ID = 1L;
    private static final String TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJBUEkgUmFzbW9vIFBsdXMiLCJzdWIiOiIxIiwiaWF0IjoxNzYwMTI5MDg2LCJleHAiOjUzNjAxMjkwODZ9.IALho86_WHlHtvFCOI-gEZaRCcJTOgg8std77nMAd2fvofC3i2yBN-MnhVQzBMUC";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "expiration", "3600000");
        ReflectionTestUtils.setField(tokenService, "secret", "$2a$12$l/a0QxcHyKOCWXjG3vWAcOeDcot.Me1JBkqWlhN5b5nte2EfG3af.");
    }

    @Test
    void given_getToken_when_userIdIsValid_then_returnToken() {
        Assertions.assertNotNull(tokenService.getToken(USER_ID));
    }

    @Test
    void given_isValid_when_tokenIsValid_then_returnTrue() {
        Assertions.assertEquals(true, tokenService.isValid(TOKEN));
    }

    @Test
    void given_isValid_when_tokenIsNotValid_then_returnFalse() {
        Assertions.assertEquals(false, tokenService.isValid(TOKEN + "Invalid"));
    }

    @Test
    void given_getUserId_when_tokenIsValid_then_returnUserId() {
        Assertions.assertEquals(1L, tokenService.getUserId(TOKEN));
    }

    @Test
    void given_getUserId_when_tokenIsNotValid_then_throwsError() {
        Assertions.assertThrows(SignatureException.class, () -> tokenService.getUserId(TOKEN + "Invalid"));
    }
}