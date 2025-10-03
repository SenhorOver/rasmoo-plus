package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.model.jpa.UserCredentials;

public interface UserDetailsService {
    UserCredentials loadUserByUsernameAndPass(String username, String pass);

    void sendRecoveryCode(String email);

    boolean recoveryCodeInValid(String recoveryCode, String email);
}
