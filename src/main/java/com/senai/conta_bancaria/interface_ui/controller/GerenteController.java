package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.*;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDto;
import com.senai.conta_bancaria.application.service.GerenteService;
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

import java.net.URI;
import java.util.List;

@Tag(name = "Gerentes", description = "Gerenciamento de gerentes do banco.")
@RestController
@RequestMapping("/api/gerente")
@RequiredArgsConstructor
public class GerenteController {
    private final GerenteService service;

    // CRUD

    // Create
    @Operation(
            summary = "Cadastrar um novo gerente",
            description = "Adiciona um novo gerente à base de dados após validações de nome, CPF, endereço de e-mail " +
                    "e senha e criação de uma conta bancária.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteRegistroDto.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "nome": "José Silva dos Santos",
                                          "cpf": 12345678910,
                                          "email": "jose@email.com",
                                          "senha": "JoseDosSantos1234"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Gerente cadastrado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Nome inválido",
                                                    value = "\"Preço mínimo do serviço deve ser R$ 50,00\""),
                                            @ExampleObject(
                                                    name = "CPF inválido",
                                                    value = "\"Duração do serviço não pode exceder 30 dias\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<GerenteResponseDto> registrarGerente(@Valid @RequestBody GerenteRegistroDto dto) {
        return ResponseEntity // retorna o código de status
                .created(URI.create("api/gerente")) // status code: 201 (criado com êxito)
                .body(service.registrarGerente(dto));
    }

    // Read
    @Operation(
            summary = "Listar todos os gerentes",
            description = "Retorna todos os gerentes cadastrados na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<GerenteResponseDto>> listarTodosOsGerentes() {
        return ResponseEntity
                .ok(service.listarTodosOsGerentes()); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Buscar gerente por CPF",
            description = "Retorna um gerente cadastrado na base de dados a partir do seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do gerente a ser buscado", example = "12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gerente encontrado."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Gerente com CPF 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @GetMapping("/{cpf}")
    public ResponseEntity<GerenteResponseDto> buscarGerente(@PathVariable Long cpf) {
        return ResponseEntity
                .ok(service.buscarGerente(cpf));
    }

    // Update
    @Operation(
            summary = "Atualizar um gerente",
            description = "Atualiza os dados de um gerente existente com novas informações.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do gerente a ser atualizado", example = "12345678910")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteAtualizacaoDto.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                        "nome": "José Silva dos Santos",
                                        "cpf": 12345678910,
                                        "email": "jose@email.com",
                                        "senha": "JoseDosSantos1234"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gerente atualizado com sucesso."),
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
                            description = "Gerente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Gerente com ID 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @PutMapping("/{cpf}")
    public ResponseEntity<GerenteResponseDto> atualizarGerente(@PathVariable Long cpf,
                                                               @Valid @RequestBody GerenteAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarGerente(cpf, dto));
    }

    // Delete
    @Operation(
            summary = "Apagar um gerente",
            description = "Remove um gerente da base de dados a partir do seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do gerente a ser apagado.", example = "12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Gerente removido com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Serviço com ID 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> apagarGerente(@PathVariable Long cpf) {
        service.apagarGerente(cpf);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }
}