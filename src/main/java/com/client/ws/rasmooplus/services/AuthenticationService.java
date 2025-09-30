package com.client.ws.rasmooplus.services;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;

public interface AuthenticationService {

    TokenDto auth(LoginDto dto);
}
