package com.client.ws.rasmooplus.controllers;

import com.client.ws.rasmooplus.dto.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.error.ErrorResponseDto;
import com.client.ws.rasmooplus.services.PaymentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Informações de pagamento", description = "Operações relacionadas a pagamentos")
@RestController
@RequestMapping(value = "/payment")
public class PaymentInfoController {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Operation(
            summary = "Faz o pagamento ao enviar requisições para a API raspay",
            description = "Faz o pagamento e depois envia um email com os as credenciais, para o usuário poder fazer login no serviço"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento feito com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(value = "/process")
    public ResponseEntity<Boolean> process(@Valid @RequestBody PaymentProcessDto dto) {
        return ResponseEntity.ok(paymentInfoService.process(dto));
    }
}
