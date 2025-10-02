package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserTypeDto;
import com.client.ws.rasmooplus.model.jpa.UserType;

public class UserTypeMapper {
    public static UserTypeDto fromEntityToDto(UserType entity) {
        return UserTypeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
