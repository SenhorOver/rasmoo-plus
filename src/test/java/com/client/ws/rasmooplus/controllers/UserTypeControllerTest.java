package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.UserTypeDto;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.services.UserTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class UserTypeControllerTest {

    @MockitoBean
    private UserTypeService userTypeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void given_findAll_then_returnAllUserType() throws Exception {
        List<UserTypeDto> userTypeList = new ArrayList<>();
        UserTypeDto userType1 = new UserTypeDto(1L, "Professor", "Professor da plataforma");
        UserTypeDto userType2 = new UserTypeDto(2L, "Administador", "Funcionário da plataforma");

        userTypeList.add(userType1);
        userTypeList.add(userType2);

        Mockito.when(userTypeService.findAll()).thenReturn(userTypeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user-type"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}