package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.dto.UserMinDto;
import com.client.ws.rasmooplus.dto.error.ErrorResponseDto;
import com.client.ws.rasmooplus.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Usuário", description = "Operações relacionadas com usuários")
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService service;

    @Operation(
            summary = "Criar novo usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(
            summary = "Fazer upload de uma foto de perfil do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload feito com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserMinDto> uploadPhoto(@PathVariable("id") Long id, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadPhoto(id, file));
    }

    @Operation(
            summary = "Faz o download da foto de perfil do usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto enviada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping(value = "/{id}/photo", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.downloadPhoto(id));
    }
}
