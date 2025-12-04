package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ClienteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDto;
import com.senai.conta_bancaria.application.dto.ClienteResponseDto;
import com.senai.conta_bancaria.application.service.ClienteService;
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

@Tag(name = "Clientes", description = "Gerenciamento de clientes do banco.")
@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    // Create
    @Operation(
            summary = "Cadastrar um novo cliente",
            description = "Adiciona um novo cliente à base de dados após validações de nome, CPF, endereço de e-mail " +
                    "e senha e criação de uma conta bancária.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDto.class),
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
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso."),
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
    public ResponseEntity<ClienteResponseDto> registrarCliente(@Valid @RequestBody ClienteRegistroDto dto) {
        return ResponseEntity // retorna o código de status
                .created(URI.create("api/cliente")) // status code: 201 (criado com êxito)
                .body(service.registrarCliente(dto));
    }

    // Read
    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna todos os clientes cadastrados na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodosOsClientes() {
        return ResponseEntity
                .ok(service.listarTodosOsClientes()); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Buscar cliente por CPF",
            description = "Retorna um cliente cadastrado na base de dados a partir do seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser buscado", example = "12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Cliente com CPF 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDto> buscarCliente(@PathVariable Long cpf) {
        return ResponseEntity
                .ok(service.buscarCliente(cpf));
    }

    // Update
    @Operation(
            summary = "Atualizar um cliente",
            description = "Atualiza os dados de um cliente existente com novas informações.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser atualizado", example = "12345678910")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteAtualizacaoDto.class),
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
    @PutMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Long cpf,
                                                               @Valid @RequestBody ClienteAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarCliente(cpf, dto));
    }

    // Delete
    @Operation(
            summary = "Apagar um cliente",
            description = "Remove um cliente da base de dados a partir do seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser apagado.", example = "12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Serviço com ID 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> apagarCliente(@PathVariable Long cpf) {
        service.apagarCliente(cpf);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }
}