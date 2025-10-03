package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.services.AuthenticationService;
import com.client.ws.rasmooplus.services.UserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<TokenDto> auth(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(service.auth(dto));
    }

    @PostMapping("/recovery-code/send")
    public ResponseEntity<Void> sendRecoveryCode(@Valid @RequestBody UserRecoveryCode dto) {
        userDetailsService.sendRecoveryCode(dto.getEmail());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/recovery-code/")
    public ResponseEntity<Boolean> recoveryCodeIsValid(@RequestParam("recoveryCode") String recoveryCode,
                                                    @RequestParam("email") String email) {
        return ResponseEntity.ok(userDetailsService.recoveryCodeInValid(recoveryCode, email));
    }



}
