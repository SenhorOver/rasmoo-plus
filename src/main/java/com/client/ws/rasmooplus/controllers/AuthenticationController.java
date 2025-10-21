package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.dto.error.ErrorResponseDto;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.services.AuthenticationService;
import com.client.ws.rasmooplus.services.UserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Operações relacionadas com autenticação e recuperação de conta")
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private UserDetailsService userDetailsService;

    @Operation(
            summary = "Realizar a autenticação do usuário"
     )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> auth(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(service.auth(dto));
    }

    @Operation(
            summary = "Enviar código de recuperação para o email do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "email enviado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(value = "/recovery-code/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendRecoveryCode(@Valid @RequestBody UserRecoveryCode dto) {
        userDetailsService.sendRecoveryCode(dto.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Validar código de recuperação"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código de recuperação válido"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping(value = "/recovery-code/")
    public ResponseEntity<Boolean> recoveryCodeIsValid(@RequestParam("recoveryCode") String recoveryCode,
                                                    @RequestParam("email") String email) {
        return ResponseEntity.ok(userDetailsService.recoveryCodeIsValid(recoveryCode, email));
    }

    @Operation(
            summary = "Atualizar senha a partir de um código de recuperação válido"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping(value = "/recovery-code/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePasswordByRecoveryCode(@Valid @RequestBody UserDetailsDto dto) {
        userDetailsService.updatePasswordByRecoveryCode(dto);
        return ResponseEntity.noContent().build();
    }

}
