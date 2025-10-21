package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.UserTypeDto;
import com.client.ws.rasmooplus.services.UserTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Tipo de usuário", description = "Operações relacionadas com tipos de usuários")
@RestController
@RequestMapping(value = "/user-type")
public class UserTypeController {

    @Autowired
    private UserTypeService service;

    @Operation(
            summary = "Buscar todos os tipos de usuário"
     )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de usuários recebidos com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<UserTypeDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

}
