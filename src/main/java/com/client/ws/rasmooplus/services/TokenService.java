package com.client.ws.rasmooplus.services;


import com.client.ws.rasmooplus.model.UserCredentials;

public interface TokenService {
    String getToken(UserCredentials auth);
}
