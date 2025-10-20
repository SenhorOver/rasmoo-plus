package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.dto.UserMinDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserDto create (UserDto dto);

    UserMinDto uploadPhoto(Long id, MultipartFile file) throws IOException;

    byte[] downloadPhoto(Long id);
}
