package com.client.ws.rasmooplus.services;


import com.client.ws.rasmooplus.model.UserCredentials;

public interface TokenService {
    String getToken(Long userId);

    Boolean isValid(String token);

    Long getUserId(String token);
}
