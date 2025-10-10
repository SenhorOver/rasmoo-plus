package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repositories.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repositories.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.services.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @Mock
    private MailIntegration mailIntegration;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private static final String USERNAME_ALUNO = "marcos@email.com";
    private static final String PASSWORD_ALUNO = "123";
    private static final String RECOVERY_CODE_ALUNO = "0000";

    private UserCredentials userCredentials;
    private UserRecoveryCode userRecoveryCode;

    @BeforeEach
    void loadData() {
        UserType userType = new UserType(1L, "Aluno", "Aluno da plataforma");
        userCredentials = new UserCredentials(1L, USERNAME_ALUNO, new BCryptPasswordEncoder().encode(PASSWORD_ALUNO), userType);
        userRecoveryCode = new UserRecoveryCode();
        userRecoveryCode.setId("1");
        userRecoveryCode.setEmail(USERNAME_ALUNO);
        userRecoveryCode.setCode(RECOVERY_CODE_ALUNO);
    }

    @Test
    void given_loadUserByUsernameAndPass_when_usernameIsValidAndPasswordIsValid_then_returnsUserCredentials() {
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));

        Assertions.assertEquals(userCredentials, userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, PASSWORD_ALUNO));

        verify(userDetailsRepository, times(1)).findByUsername(USERNAME_ALUNO);
    }

    @Test
    void given_loadUserByUsernameAndPass_when_usernameIsNotValid_then_throwsBadRequestException() {
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.empty());

        Assertions.assertEquals("User/Password Invalid",Assertions.assertThrows(
                BadRequestException.class,
                () -> userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, PASSWORD_ALUNO)
        ).getLocalizedMessage());

        verify(userDetailsRepository, times(1)).findByUsername(USERNAME_ALUNO);
    }

    @Test
    void given_loadUserByUsernameAndPass_when_usernameIsValidAndPasswordIsNotValid_then_throwsBadRequestException() {
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));

        Assertions.assertThrows(BadRequestException.class, () -> userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, "1234"));

        verify(userDetailsRepository, times(1)).findByUsername(USERNAME_ALUNO);
    }


    @Test
    void given_sendRecoveryCode_when_emailIsFoundOnCache_then_updateCode(){
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));
        when(userRecoveryCodeRepository.save(any())).thenReturn(null);

        Assertions.assertDoesNotThrow(() -> userDetailsService.sendRecoveryCode(USERNAME_ALUNO));
        verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME_ALUNO);
        verify(userRecoveryCodeRepository, times(1)).save(any());
        verify(mailIntegration, times(1)).send(any(), any(), any());
    }

    @Test
    void given_sendRecoveryCode_when_emailIsNotFoundOnCacheAndFoundOnUserCredentialsTable_then_createNewCode(){
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));
        when(userRecoveryCodeRepository.save(any())).thenReturn(null);

        Assertions.assertDoesNotThrow(() -> userDetailsService.sendRecoveryCode(USERNAME_ALUNO));
        verify(mailIntegration, times(1)).send(any(), any(), any());
    }

    @Test
    void given_sendRecoveryCode_when_emailIsNotFoundOnCacheAndNotFoundOnUserCredentialsTable_then_throwsNotFoundException(){
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userDetailsService.sendRecoveryCode(USERNAME_ALUNO));
        verify(mailIntegration, times(0)).send(any(), any(), any());
    }

    @Test
    void given_recoveryCodeIsValid_when_userIsFound_then_returnTrue() {
        // Dar valor para variables que vem do @Value
        ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));

        Assertions.assertTrue(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE_ALUNO, USERNAME_ALUNO));

        verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME_ALUNO);
    }

    @Test
    void given_recoveryCodeIsValid_when_userIsFoundAndCodeIsExpired_then_returnFalse() {
        ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
        userRecoveryCode.setCreationDate(LocalDateTime.now().minusMinutes(6));
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));

        Assertions.assertFalse(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE_ALUNO, USERNAME_ALUNO));

        verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME_ALUNO);
    }

    @Test
    void given_recoveryCodeIsValid_when_userIsFoundAndCodeIsInvalid_then_returnFalse() {
        ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));

        Assertions.assertFalse(userDetailsService.recoveryCodeIsValid("0001", USERNAME_ALUNO));

        verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME_ALUNO);
    }

    @Test
    void given_recoveryCodeIsValid_when_userIsNotFound_then_throwsNotFoundException() {
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userDetailsService.recoveryCodeIsValid(RECOVERY_CODE_ALUNO, USERNAME_ALUNO));

        verify(userRecoveryCodeRepository, times(1)).findByEmail(USERNAME_ALUNO);
    }

    @Test
    void given_updatePasswordByRecoveryCode_when_RecoveryCodeIsValid_then_updatePassword() {
        ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO))
                .thenReturn(Optional.of(userRecoveryCode));
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO))
                .thenReturn(Optional.of(userCredentials));

        UserDetailsDto userDetailsDto = new UserDetailsDto(
                USERNAME_ALUNO, PASSWORD_ALUNO, RECOVERY_CODE_ALUNO
        );

        Assertions.assertDoesNotThrow(() ->
                userDetailsService.updatePasswordByRecoveryCode(userDetailsDto)
        );

        verify(userDetailsRepository, times(1)).findByUsername(USERNAME_ALUNO);
        verify(userDetailsRepository, times(1)).save(any(UserCredentials.class));
    }

    @Test
    void given_updatePasswordByRecoveryCode_when_RecoveryCodeIsNotValid_then_updatePassword() {
        ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO))
                .thenReturn(Optional.of(userRecoveryCode));

        UserDetailsDto userDetailsDto = new UserDetailsDto(
                USERNAME_ALUNO, PASSWORD_ALUNO, "0001"
        );

        Assertions.assertThrows(BadRequestException.class, () ->
                userDetailsService.updatePasswordByRecoveryCode(userDetailsDto)
        );

        verify(userDetailsRepository, times(0)).findByUsername(USERNAME_ALUNO);
        verify(userDetailsRepository, times(0)).save(any(UserCredentials.class));
    }
}