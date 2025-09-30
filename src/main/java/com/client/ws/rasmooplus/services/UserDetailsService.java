package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.model.UserCredentials;

public interface UserDetailsService {
    UserCredentials loadUserByUsernameAndPass(String username, String pass);
}
