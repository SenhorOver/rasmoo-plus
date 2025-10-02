package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.dto.UserTypeDto;
import com.client.ws.rasmooplus.mapper.UserTypeMapper;
import com.client.ws.rasmooplus.repositories.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.services.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTypeServiceImpl implements UserTypeService {

    @Autowired
    private UserTypeRepository repository;

    @Transactional
    @Override
    public List<UserTypeDto> findAll() {
        return repository.findAll().stream().map(UserTypeMapper::fromEntityToDto).toList();

    }
}
