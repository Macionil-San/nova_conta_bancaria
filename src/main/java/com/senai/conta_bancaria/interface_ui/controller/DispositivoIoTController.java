package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.DispositivoIoTDto;
import com.senai.conta_bancaria.application.dto.DispositivoIoTResponseDto;
import com.senai.conta_bancaria.application.dto.ValidacaoCodigoDto;
import com.senai.conta_bancaria.application.service.DispositivoIoTService;
import com.senai.conta_bancaria.domain.service.AutenticacaoIoTService;
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
import java.util.Map;

@RestController
@RequestMapping("/dispositivos-iot")
@RequiredArgsConstructor
@Tag(name = "Dispositivos IoT", description = "Gerenciamento de dispositivos IoT para autenticação biométrica")
@SecurityRequirement(name = "bearer-jwt")
public class DispositivoIoTController {
    
    private final DispositivoIoTService dispositivoService;
    private final AutenticacaoIoTService autenticacaoService;
    
    @PostMapping
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Cadastrar dispositivo IoT", description = "Apenas gerentes podem cadastrar dispositivos")
    public ResponseEntity<DispositivoIoTResponseDto> cadastrarDispositivo(@Valid @RequestBody DispositivoIoTDto dto) {
        DispositivoIoTResponseDto dispositivo = dispositivoService.cadastrarDispositivo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dispositivo);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Listar todos os dispositivos")
    public ResponseEntity<List<DispositivoIoTResponseDto>> listarTodos() {
        List<DispositivoIoTResponseDto> dispositivos = dispositivoService.listarTodos();
        return ResponseEntity.ok(dispositivos);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN') or hasRole('CLIENTE')")
    @Operation(summary = "Buscar dispositivo por ID")
    public ResponseEntity<DispositivoIoTResponseDto> buscarPorId(@PathVariable String id) {
        DispositivoIoTResponseDto dispositivo = dispositivoService.buscarPorId(id);
        return ResponseEntity.ok(dispositivo);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN') or hasRole('CLIENTE')")
    @Operation(summary = "Buscar dispositivo por cliente")
    public ResponseEntity<DispositivoIoTResponseDto> buscarPorCliente(@PathVariable String clienteId) {
        DispositivoIoTResponseDto dispositivo = dispositivoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(dispositivo);
    }
    
    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Desativar dispositivo")
    public ResponseEntity<Void> desativarDispositivo(@PathVariable String id) {
        dispositivoService.desativarDispositivo(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Ativar dispositivo")
    public ResponseEntity<Void> ativarDispositivo(@PathVariable String id) {
        dispositivoService.ativarDispositivo(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/validar-codigo")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Validar código de autenticação IoT", 
               description = "Valida o código recebido do dispositivo IoT após autenticação biométrica")
    public ResponseEntity<Map<String, Boolean>> validarCodigo(@Valid @RequestBody ValidacaoCodigoDto dto) {
        boolean valido = autenticacaoService.validarCodigo(dto.getClienteId(), dto.getCodigo());
        return ResponseEntity.ok(Map.of("valido", valido));
    }
}
