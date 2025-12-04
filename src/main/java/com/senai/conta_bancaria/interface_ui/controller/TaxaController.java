package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.TaxaDto;
import com.senai.conta_bancaria.application.dto.TaxaResponseDto;
import com.senai.conta_bancaria.application.service.TaxaService;
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
@RequestMapping("/taxas")
@RequiredArgsConstructor
@Tag(name = "Taxas", description = "Gerenciamento de taxas bancárias")
@SecurityRequirement(name = "bearer-jwt")
public class TaxaController {
    
    private final TaxaService taxaService;
    
    @PostMapping
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Criar nova taxa", description = "Apenas gerentes podem criar taxas")
    public ResponseEntity<TaxaResponseDto> criarTaxa(@Valid @RequestBody TaxaDto dto) {
        TaxaResponseDto taxa = taxaService.criarTaxa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(taxa);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN') or hasRole('CLIENTE')")
    @Operation(summary = "Listar todas as taxas")
    public ResponseEntity<List<TaxaResponseDto>> listarTodas() {
        List<TaxaResponseDto> taxas = taxaService.listarTodas();
        return ResponseEntity.ok(taxas);
    }
    
    @GetMapping("/ativas")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN') or hasRole('CLIENTE')")
    @Operation(summary = "Listar taxas ativas")
    public ResponseEntity<List<TaxaResponseDto>> listarAtivas() {
        List<TaxaResponseDto> taxas = taxaService.listarAtivas();
        return ResponseEntity.ok(taxas);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN') or hasRole('CLIENTE')")
    @Operation(summary = "Buscar taxa por ID")
    public ResponseEntity<TaxaResponseDto> buscarPorId(@PathVariable String id) {
        TaxaResponseDto taxa = taxaService.buscarPorId(id);
        return ResponseEntity.ok(taxa);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Atualizar taxa", description = "Apenas gerentes podem atualizar taxas")
    public ResponseEntity<TaxaResponseDto> atualizarTaxa(@PathVariable String id, @Valid @RequestBody TaxaDto dto) {
        TaxaResponseDto taxa = taxaService.atualizarTaxa(id, dto);
        return ResponseEntity.ok(taxa);
    }
    
    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Desativar taxa", description = "Apenas gerentes podem desativar taxas")
    public ResponseEntity<Void> desativarTaxa(@PathVariable String id) {
        taxaService.desativarTaxa(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    @Operation(summary = "Ativar taxa", description = "Apenas gerentes podem ativar taxas")
    public ResponseEntity<Void> ativarTaxa(@PathVariable String id) {
        taxaService.ativarTaxa(id);
        return ResponseEntity.noContent().build();
    }
}
