package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;

import java.util.Objects;

public class UserMapper {

    public static User fromDtoToEntity(UserDto dto, UserType userType, SubscriptionType subscriptionType) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dtSubscription(dto.getDtSubscription())
                .dtExpiration(dto.getDtExpiration())
                .userType(userType)
                .subscriptionType(subscriptionType)
                .build();
    }

    public static UserDto fromEntityToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .dtSubscription(entity.getDtSubscription())
                .dtExpiration(entity.getDtExpiration())
                .userType(UserTypeMapper.fromEntityToDto(entity.getUserType()))
                .userTypeId(entity.getUserType().getId())
                .subscriptionType(Objects.nonNull(entity.getSubscriptionType()) ? SubscriptionTypeMapper.fromEntityToDto(entity.getSubscriptionType()) : null)
                .subscriptionTypeId(Objects.nonNull(entity.getSubscriptionType()) ? entity.getSubscriptionType().getId() : null)
                .build();
    }
}
