package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(SubscriptionTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class SubscriptionTypeControllerTest {

    @MockitoBean
    private SubscriptionTypeService subscriptionTypeService;

    @Autowired
    private MockMvc mockMvc;

    private static final Long ID = 1L;

    @Test
    void given_findAll_then_returnAllSubscriptionType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type")).andExpect(
                status().isOk()
        );

    }

    @Test
    void given_findById_when_getId1_then_returnOneSubscriptionType() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(ID, "VITALICIO", null, BigDecimal.valueOf(997), "FOREVER2025");

        Mockito.when(subscriptionTypeService.findById(1L)).thenReturn(subscriptionTypeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type/1").contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("VITALICIO")))
                ;

    }

}