package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
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

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long ID = 1L;

    @Test
    void given_findAll_then_returnAllSubscriptionType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type"))
                .andExpect(status().isOk());

    }

    @Test
    void given_findById_when_getId1_then_returnOneSubscriptionType() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(ID, "VITALICIO", null, BigDecimal.valueOf(997), "FOREVER2025");

        Mockito.when(subscriptionTypeService.findById(ID)).thenReturn(subscriptionTypeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type/{id}", 1))
//                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("VITALICIO")))
        ;

    }

    @Test
    void given_delete_when_GetId1_then_noReturnAndNoContent() throws Exception {
        Mockito.doNothing().when(subscriptionTypeService).delete(ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/subscription-type/{id}", 1))
                .andExpect(status().isNoContent());

        Mockito.verify(subscriptionTypeService, Mockito.times(1)).delete(ID);

    }

    @Test
    void given_create_when_DtoIsOk_then_returnSubscriptionTypeCreated() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(null, "VITALICIO", null, BigDecimal.valueOf(997), "FOREVER2025");
        SubscriptionTypeDto subscriptionTypeDtoCreated = new SubscriptionTypeDto(ID, "VITALICIO", null, BigDecimal.valueOf(997), "FOREVER2025");

        Mockito.when(subscriptionTypeService.create(subscriptionTypeDto)).thenReturn(subscriptionTypeDtoCreated);

        mockMvc.perform(MockMvcRequestBuilders.post("/subscription-type")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto))
                )
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));

    }

    @Test
    void given_create_when_DtoIsMissingValues_then_returnBadRequest() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(null, "a", 13, null, "22");

        mockMvc.perform(MockMvcRequestBuilders.post("/subscription-type")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto))
                )
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("[price=não pode ser nulo, accessMonths=não pode ser maior que 12, name=deve ter um valor entre 5 e 30 caracteres, productKey=deve ter um valor entre 5 e 15 caracteres]")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(400)))
        ;

        Mockito.verify(subscriptionTypeService, Mockito.times(0)).create(any());
    }

    @Test
    void given_update_when_DtoIsOk_then_returnSubscriptionTypeUpdated() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(ID, "VITALICIO", null, BigDecimal.valueOf(997), "FOREVER2025");

        Mockito.when(subscriptionTypeService.update(ID, subscriptionTypeDto)).thenReturn(subscriptionTypeDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/subscription-type/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto))
                )
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));

    }

    @Test
    void given_update_when_DtoIsMissingValues_then_returnBadRequest() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(ID, "a", 13, null, "22");

        mockMvc.perform(MockMvcRequestBuilders.put("/subscription-type/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto))
                )
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("[price=não pode ser nulo, accessMonths=não pode ser maior que 12, name=deve ter um valor entre 5 e 30 caracteres, productKey=deve ter um valor entre 5 e 15 caracteres]")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(400)))
        ;

        Mockito.verify(subscriptionTypeService, Mockito.times(0)).update(any(), any());
    }

    @Test
    void given_update_when_idIsNull_then_returnBadRequest() throws Exception {
        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(ID, "a", 13, null, "22");

        mockMvc.perform(MockMvcRequestBuilders.put("/subscription-type")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto))
                )
                .andExpect(status().isMethodNotAllowed());

        Mockito.verify(subscriptionTypeService, Mockito.times(0)).update(any(), any());
    }

}