package com.client.ws.rasmooplus.mapper.wsraspay;

import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.model.User;

public class CustomerMapper {
    public static CustomerDto build(User user) {
        String[] fullname = user.getName().split(" ");
        String firstName = fullname[0];
        String lastName = fullname.length > 1 ? fullname[fullname.length - 1] : "";

        return CustomerDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(user.getEmail())
                .cpf(user.getCpf())
                .build();
    }
}
