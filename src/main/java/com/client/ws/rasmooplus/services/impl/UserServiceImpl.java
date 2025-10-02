package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.UserDto;
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

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

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
}
