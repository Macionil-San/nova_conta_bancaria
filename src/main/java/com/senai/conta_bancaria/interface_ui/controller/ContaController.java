package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.*;
import com.senai.conta_bancaria.application.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Contas", description = "Gerenciamento de contas bancárias.")
@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService service;

    // CRUD

    // Create: embutido em Cliente

    // Read
    @Operation(
            summary = "Listar todas as contas",
            description = "Retorna uma lista com todas as contas cadastrada na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contas encontradas."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Contas não encontradas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Contas não encontradas.\"")
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ContaResumoDto>> listarTodasAsContas() {
        return ResponseEntity
                .ok(service.listarTodasAsContas()); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Listar todas as contas por CPF",
            description = "Retorna uma lista com todas as contas de um usuário por CPF cadastradas na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contas encontradas."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Contas não encontradas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Contas de cliente com CPF 19876543210 não " +
                                            "foram encontradas.\"")
                            )
                    )
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<ContaResumoDto>> listarContasPorCpf(@PathVariable Long cpf) {
        return ResponseEntity
                .ok(service.listarContasPorCpf(cpf)); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Buscar conta por número",
            description = "Retorna uma conta cadastradas na base de dados a partir do seu número.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta a ser buscada", example = "102030")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com CPF 102030 não encontrada.\"")
                            )
                    )
            }
    )
    @GetMapping("/numero/{numero}")
    public ResponseEntity<ContaResumoDto> buscarConta(@PathVariable Long numero) {
        return ResponseEntity
                .ok(service.buscarConta(numero));
    }

    // Update
    @Operation(
            summary = "Atualizar uma conta",
            description = "Atualiza os dados de uma conta existente com novas informações.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta a ser atualizado", example = "102030")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteAtualizacaoDto.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                        "saldo": 1000,
                                        "limite": 100,
                                        "taxa": 2,
                                        "rendimento": null
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Preço inválido",
                                                    value = "\"Preço mínimo do serviço deve ser R$ 50,00\""),
                                            @ExampleObject(
                                                    name = "Duração excedida",
                                                    value = "\"Duração do serviço não pode exceder 30 dias\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Cliente com ID 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @PutMapping("/{numero}")
    public ResponseEntity<ContaResumoDto> atualizarConta(@PathVariable Long numero,
                                                         @Valid @RequestBody ContaAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarConta(numero, dto));
    }

    // Delete
    @Operation(
            summary = "Apagar uma conta",
            description = "Remove uma conta bancária da base de dados a partir do seu número.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta a ser apagada.", example = "102030")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta removida com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 102030 não encontrada.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> apagarConta(@PathVariable Long numero) {
        service.apagarConta(numero);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }

    // Ações específicas

    @PostMapping("/{numero}/sacar")
    public ResponseEntity<ContaResumoDto> sacar(@PathVariable Long numero,
                                                @Valid @RequestBody ValorSaqueDepositoDto dto) {
        return ResponseEntity
                .ok(service.sacar(numero, dto));
    }

    @PostMapping("/{numero}/depositar")
    public ResponseEntity<ContaResumoDto> depositar(@PathVariable Long numero,
                                                    @Valid @RequestBody ValorSaqueDepositoDto dto) {
        return ResponseEntity
                .ok(service.depositar(numero, dto));
    }

    @PostMapping("/{numero}/transferir")
    public ResponseEntity<ContaResumoDto> transferir(@PathVariable Long numero,
                                                     @Valid @RequestBody TransferenciaDto dto) {
        return ResponseEntity
                .ok(service.transferir(numero, dto));
    }

    @PostMapping("/{numero}/rendimento")
    public ResponseEntity<ContaResumoDto> rendimento(@PathVariable Long numero) {
        return ResponseEntity
                .ok(service.rendimento(numero));
    }
}