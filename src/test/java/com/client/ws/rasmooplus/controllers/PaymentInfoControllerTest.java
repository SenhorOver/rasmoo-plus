package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.services.PaymentInfoService;
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


@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(PaymentInfoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class PaymentInfoControllerTest {

    @MockitoBean
    private PaymentInfoService paymentInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void given_process_then_returnOk() throws Exception {
        PaymentProcessDto paymentProcessDto = new PaymentProcessDto();

        Mockito.when(paymentInfoService.process(paymentProcessDto)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/payment/process")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(paymentProcessDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                ;

        Mockito.verify(paymentInfoService, Mockito.times(1)).process(paymentProcessDto);
    }
}