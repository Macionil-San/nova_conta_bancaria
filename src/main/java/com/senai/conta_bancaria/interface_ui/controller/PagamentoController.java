package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.PagamentoDto;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDto;
import com.senai.conta_bancaria.application.service.PagamentoAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos de boletos e contas")
@SecurityRequirement(name = "bearer-jwt")
public class PagamentoController {
    
    private final PagamentoAppService pagamentoAppService;
    
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Realizar pagamento", description = "Clientes podem realizar pagamentos de suas contas")
    public ResponseEntity<PagamentoResponseDto> realizarPagamento(@Valid @RequestBody PagamentoDto dto) {
        PagamentoResponseDto pagamento = pagamentoAppService.realizarPagamento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Listar todos os pagamentos", description = "Apenas gerentes podem ver todos os pagamentos")
    public ResponseEntity<List<PagamentoResponseDto>> listarTodos() {
        List<PagamentoResponseDto> pagamentos = pagamentoAppService.listarTodos();
        return ResponseEntity.ok(pagamentos);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Buscar pagamento por ID")
    public ResponseEntity<PagamentoResponseDto> buscarPorId(@PathVariable String id) {
        PagamentoResponseDto pagamento = pagamentoAppService.buscarPorId(id);
        return ResponseEntity.ok(pagamento);
    }
    
    @GetMapping("/conta/{contaId}")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Listar pagamentos por conta")
    public ResponseEntity<List<PagamentoResponseDto>> listarPorConta(@PathVariable String contaId) {
        List<PagamentoResponseDto> pagamentos = pagamentoAppService.listarPorConta(contaId);
        return ResponseEntity.ok(pagamentos);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Listar pagamentos por cliente")
    public ResponseEntity<List<PagamentoResponseDto>> listarPorCliente(@PathVariable String clienteId) {
        List<PagamentoResponseDto> pagamentos = pagamentoAppService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pagamentos);
    }
}
