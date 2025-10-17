package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto create (UserDto dto);

    UserDto uploadPhoto(Long id, MultipartFile file);
}
