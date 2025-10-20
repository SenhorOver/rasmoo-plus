package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.dto.UserMinDto;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
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
        UserType userType = getUserType();

        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));

        User user = getUser(userType);

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

    @Test
    void given_uploadPhoto_when_thereIsUserAndFileAndIsIsPNGOrJPEG_then_updatePhotoAndReturnUser() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/static/logoJava.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "logoJava.jpeg", MediaType.MULTIPART_FORM_DATA_VALUE, fis);

        User user = getUser(getUserType());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        user.setPhoto(file.getBytes());
        user.setPhotoName(file.getOriginalFilename());
        when(userRepository.save(user)).thenReturn(user);

        UserMinDto userReturned = userService.uploadPhoto(1L, file);

        Assertions.assertNotNull(userReturned);
        Assertions.assertNotNull(userReturned.getPhoto());
        Assertions.assertEquals("logoJava.jpeg", userReturned.getPhotoName());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void given_uploadPhoto_when_thereIsUserAndFileAndIsIsNotPNGOrJPEG_then_throwBadRequestException() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/static/logoJava.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "logoJava.txt", MediaType.MULTIPART_FORM_DATA_VALUE, fis);

        Assertions.assertThrows(BadRequestException.class, () -> userService.uploadPhoto(1L, file));

        verify(userRepository, times(0)).findById(any());
        verify(userRepository, times(0)).save(any());
    }


    @Test
    void given_downloadPhoto_when_thereIsUserAndPhoto_then_returnByteArray() {
        User user = getUser(getUserType());
        user.setPhoto(new byte[0]);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertNotNull(userService.downloadPhoto(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void given_downloadPhoto_when_thereIsUserAndThereIsNoPhoto_then_throwBadRequestException() {
        User user = getUser(getUserType());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(BadRequestException.class, () -> userService.downloadPhoto(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    private static UserType getUserType() {
        return new UserType(1L, "Aluno", "Aluno da plataforma");
    }

    private User getUser(UserType userType) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setDtSubscription(dto.getDtSubscription());
        user.setDtExpiration(dto.getDtExpiration());
        user.setUserType(userType);
        return user;
    }
}
