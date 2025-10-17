package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void given_create_when_dtoIsValid_then_returnUserCreated() throws Exception {
        UserDto userDto = createUserDto();
        UserDto userDtoCreated = createUserDto();
        userDtoCreated.setId(1L);

        Mockito.when(userService.create(userDto)).thenReturn(userDtoCreated);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        ;

        Mockito.verify(userService, Mockito.times(1)).create(userDto);
    }

    @Test
    void given_create_when_dtoIsNotValid_then_returnBadRequest() throws Exception {
        UserDto userDto = new UserDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto))
                )
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("[userTypeId=must not be null, name=valor não pode ser nulo ou vazio]")))
               .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
               .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(400)))
        ;

        Mockito.verify(userService, Mockito.times(0)).create(any());
    }

    @Test
    void given_uploadPhoto_then_recieveMultiPartFIle_then_return200Ok() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/static/logoJava.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "logoJava.png", MediaType.MULTIPART_FORM_DATA_VALUE, fis);

        Mockito.when(userService.uploadPhoto(1L, file)).thenReturn(new UserDto());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/user/1/upload-photo");
        builder.with(request -> {
            request.setMethod(HttpMethod.PATCH.name());
            return request;
        });
        mockMvc.perform(builder
                        .file(file)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("nome sobrenome");
        userDto.setEmail("email@email.com");
        userDto.setPhone("11999998888");
        userDto.setCpf("96506102004");
        userDto.setUserTypeId(3L);
        return userDto;
    }
}