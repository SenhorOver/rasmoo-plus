package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.services.AuthenticationService;
import com.client.ws.rasmooplus.services.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;


@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class AuthenticationControllerTest {

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password123";
    private static final String RECOVERY_CODE = "0001";

    @Test
    void given_auth_when_dtoIsValid_then_returnSuccess() throws Exception {
        LoginDto loginDto = new LoginDto(EMAIL, PASSWORD);
        TokenDto tokenDto = new TokenDto("token", "Bearer");

        Mockito.when(authenticationService.auth(loginDto)).thenReturn(tokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDto))
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.is("token")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("Bearer")))
        ;

        Mockito.verify(authenticationService, Mockito.times(1)).auth(loginDto);
    }
    @Test
    void given_auth_when_dtoIsNotValid_then_noReturnAndBadRequest() throws Exception {
        LoginDto loginDto = new LoginDto("", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDto))
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("[password=required attribute, username=required attribute]")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(400)))
        ;

        Mockito.verify(authenticationService, Mockito.times(0)).auth(loginDto);
    }

    @Test
    void given_sendRecoveryCode_when_dtoIsValid_then_returnNoContent() throws Exception {
        UserRecoveryCode userRecoveryCode = new UserRecoveryCode("1", EMAIL, RECOVERY_CODE, LocalDateTime.now());

        Mockito.doNothing().when(userDetailsService).sendRecoveryCode(userRecoveryCode.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/recovery-code/send")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRecoveryCode))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        ;

        Mockito.verify(userDetailsService, Mockito.times(1)).sendRecoveryCode(userRecoveryCode.getEmail());
    }

    @Test
    void given_sendRecoveryCode_when_dtoIsNotValid_then_noReturnAndBadRequest() throws Exception {
        UserRecoveryCode userRecoveryCode = new UserRecoveryCode("", "email", "", null);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/recovery-code/send")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRecoveryCode))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("[email=must be a well-formed email address]")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(400)))
        ;

        Mockito.verify(userDetailsService, Mockito.times(0)).sendRecoveryCode(any());
    }

    @Test
    void given_updatePasswordByRecoveryCode_when_dtoIsValid_then_returnNoContent() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto(EMAIL, PASSWORD, RECOVERY_CODE);

        Mockito.doNothing().when(userDetailsService).updatePasswordByRecoveryCode(userDetailsDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/auth/recovery-code/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDetailsDto))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        ;

        Mockito.verify(userDetailsService, Mockito.times(1)).updatePasswordByRecoveryCode(userDetailsDto);
    }

    @Test
    void given_updatePasswordByRecoveryCode_when_dtoIsNotValid_then_noReturnAndBadRequest() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto("email", "", "");

        mockMvc.perform(MockMvcRequestBuilders.patch("/auth/recovery-code/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDetailsDto))
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("[password=inválido, email=inválido]")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(400)))
        ;

        Mockito.verify(userDetailsService, Mockito.times(0)).updatePasswordByRecoveryCode(any());
    }

    @Test
    void given_recoveryCodeIsValid_when_queryParamsExists_then_returnSuccess() throws Exception {
        Mockito.when(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, EMAIL)).thenReturn(true);

//        mockMvc.perform(MockMvcRequestBuilders.get("/auth/recovery-code/?recoveryCode=0001&email=email@email.com"))
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/recovery-code/")
                        .param("recoveryCode",RECOVERY_CODE)
                        .param("email", EMAIL)
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        Mockito.verify(userDetailsService, Mockito.times(1)).recoveryCodeIsValid(RECOVERY_CODE, EMAIL);
    }
    @Test
    void given_recoveryCodeIsValid_when_queryParamsNotExists_then_returnBadRequest() throws Exception {
        Mockito.when(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, EMAIL)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/recovery-code/"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
        ;

        Mockito.verify(userDetailsService, Mockito.times(0)).recoveryCodeIsValid(any(), any());
    }
    
}