package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.UserTypeDto;

import java.util.List;

public interface UserTypeService {
    List<UserTypeDto> findAll();
}
