package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.dto.UserMinDto;
import com.client.ws.rasmooplus.exceptions.BadRequestException;
import com.client.ws.rasmooplus.exceptions.NotFoundException;
import com.client.ws.rasmooplus.mapper.UserMapper;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repositories.jpa.UserRepository;
import com.client.ws.rasmooplus.repositories.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final String PNG = ".png";
    private static final String JPEG = ".jpeg";

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserTypeRepository userTypeRepository;

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        if(Objects.nonNull(dto.getId())) {
            throw new BadRequestException("id deve ser nulo");
        }

        UserType userType = userTypeRepository.findById(dto.getUserTypeId()).orElseThrow(() -> new NotFoundException("userTypeId não encontrado"));

        User user = UserMapper.fromDtoToEntity(dto, userType, null);

        return UserMapper.fromEntityToDto(repository.save(user));
    }

    @Transactional
    @Override
    public UserMinDto uploadPhoto(Long id, MultipartFile file) throws IOException {
        String imgName = file.getOriginalFilename();
        String formatPNG = imgName.substring(imgName.length() - 4);
        String formatJPEG = imgName.substring(imgName.length() - 5);

        if(!(PNG.equalsIgnoreCase(formatPNG) || JPEG.equalsIgnoreCase(formatJPEG))) {
            throw new BadRequestException("Image need to be JPEG or PNG");
        }

        User user = findById(id);

        user.setPhotoName(file.getOriginalFilename());
        user.setPhoto(file.getBytes());

        user = repository.save(user);

        return new UserMinDto(user.getId(), user.getPhoto(), user.getPhotoName());
    }

    @Transactional
    @Override
    public byte[] downloadPhoto(Long id) {
        User user = findById(id);
        if(Objects.isNull(user.getPhoto())) {
            throw new BadRequestException("User does not have photo");
        }

        return user.getPhoto();
    }

    private User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found"));
    }
}
