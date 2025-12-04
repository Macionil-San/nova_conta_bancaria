package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.GerenteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDto;
import com.senai.conta_bancaria.application.dto.GerenteResponseDto;
import com.senai.conta_bancaria.domain.entity.Gerente;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GerenteService {
    private final GerenteRepository repository;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto registrarGerente(GerenteRegistroDto dto) {
        Gerente gerenteRegistrado = repository // verifica se o gerente já existe
                .findByCpfAndAtivoTrue(dto.cpf())
                .orElseGet( // se não existir, cria um novo
                        () -> repository.save(dto.toEntity())
                );

        gerenteRegistrado.setSenha(passwordEncoder.encode(dto.senha()));
        return GerenteResponseDto.fromEntity(repository.save(gerenteRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public List<GerenteResponseDto> listarTodosOsGerentes() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(GerenteResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public GerenteResponseDto buscarGerente(Long cpf) {
        return GerenteResponseDto.fromEntity(procurarGerenteAtivo(cpf));
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto atualizarGerente(Long cpf, GerenteAtualizacaoDto dto) {
        Gerente gerente = procurarGerenteAtivo(cpf);

        gerente.setNome(dto.nome());
        gerente.setCpf(dto.cpf());
        gerente.setEmail(dto.email());
        gerente.setSenha(dto.senha());

        return GerenteResponseDto.fromEntity(repository.save(gerente));
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    public void apagarGerente(Long cpf) {
        Gerente gerente = procurarGerenteAtivo(cpf);

        gerente.setAtivo(false);

        repository.save(gerente);
    }

    // Mét0do auxiliar para as requisições
    private Gerente procurarGerenteAtivo(Long cpf) {
        return repository
                .findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("gerente"));
    }
}