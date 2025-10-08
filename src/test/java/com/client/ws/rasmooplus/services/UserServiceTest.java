package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.dto.UserTypeDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repositories.jpa.UserRepository;
import com.client.ws.rasmooplus.repositories.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto dto;

    @BeforeEach
        void loadUser() {
        dto = new UserDto();
        dto.setEmail("marcos@email.com");
        dto.setCpf("11111111111");
        dto.setUserTypeId(1L);
    }


    @Test
    void given_create_when_idIsNullAndUserTypeIsFound_then_returnUserCreated(){
        UserType userType = new UserType(1L, "Aluno", "Aluno da plataforma");

        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setDtSubscription(dto.getDtSubscription());
        user.setDtExpiration(dto.getDtExpiration());
        user.setUserType(userType);

        when(userRepository.save(user)).thenReturn(user);

        UserTypeDto userTypeDto = new UserTypeDto();
        userTypeDto.setId(user.getUserType().getId());
        userTypeDto.setName(user.getUserType().getName());
        userTypeDto.setDescription(user.getUserType().getDescription());

        dto.setUserType(userTypeDto);

        Assertions.assertEquals(dto, userService.create(dto));

        // Verificar valor com certa quantidade de vezes e com o determinado valor
        verify(userTypeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void given_create_when_idIsNotNull_then_throwBadRequestException(){
        dto.setId(1L);
        Assertions.assertThrows(BadRequestException.class, () -> userService.create(dto));

        // Verificar valor com certa quantidade de vezes e com o determinado valor
        verify(userTypeRepository, times(0)).findById(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void given_create_when_idIsNullAndUserTypeIsNotFound_then_throwNotFoundException(){
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.create(dto));

        // Verificar valor com certa quantidade de vezes e com o determinado valor
        verify(userTypeRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(any());
    }
}