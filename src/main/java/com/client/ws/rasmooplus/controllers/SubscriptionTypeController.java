package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.dto.error.ErrorResponseDto;
import com.client.ws.rasmooplus.services.SubscriptionTypeService;
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

import java.util.List;

@Tag(name = "Tipos de assinatura", description = "Operações relacionadas com tipos de assinaturas disponíveis")
@RestController
@RequestMapping(value = "/subscription-type")
public class SubscriptionTypeController {

    @Autowired
    private SubscriptionTypeService service;

    @Operation(
            summary = "Buscar todos os tipos de assinatura"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de assinatura recebidos com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<SubscriptionTypeDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Buscar um tipo de assinatura por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de assintaura recebido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Assinatura nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionTypeDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Criar um novo tipo de assinatura"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assinatura criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionTypeDto> create(@Valid @RequestBody SubscriptionTypeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(
            summary = "Atualizar um tipo de assinatura por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assinatura atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Assinatura não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionTypeDto> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody SubscriptionTypeDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Deletar um tipo de assinatura por ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assinatura deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Assinatura não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
