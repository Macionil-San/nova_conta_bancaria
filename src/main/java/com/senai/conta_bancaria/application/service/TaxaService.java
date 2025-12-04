package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.TaxaDto;
import com.senai.conta_bancaria.application.dto.TaxaResponseDto;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import com.senai.conta_bancaria.domain.service.PagamentoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxaService {
    
    private final TaxaRepository taxaRepository;
    private final PagamentoDomainService pagamentoDomainService;
    
    @Transactional
    public TaxaResponseDto criarTaxa(TaxaDto dto) {
        Taxa taxa = Taxa.builder()
                .descricao(dto.getDescricao())
                .percentual(dto.getPercentual())
                .valorFixo(dto.getValorFixo())
                .ativo(true)
                .build();
        
        pagamentoDomainService.validarTaxa(taxa);
        
        Taxa taxaSalva = taxaRepository.save(taxa);
        return converterParaDto(taxaSalva);
    }
    
    @Transactional(readOnly = true)
    public List<TaxaResponseDto> listarTodas() {
        return taxaRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TaxaResponseDto> listarAtivas() {
        return taxaRepository.findByAtivoTrue().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TaxaResponseDto buscarPorId(String id) {
        Taxa taxa = taxaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa não encontrada"));
        return converterParaDto(taxa);
    }
    
    @Transactional
    public TaxaResponseDto atualizarTaxa(String id, TaxaDto dto) {
        Taxa taxa = taxaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa não encontrada"));
        
        taxa.setDescricao(dto.getDescricao());
        taxa.setPercentual(dto.getPercentual());
        taxa.setValorFixo(dto.getValorFixo());
        
        pagamentoDomainService.validarTaxa(taxa);
        
        Taxa taxaAtualizada = taxaRepository.save(taxa);
        return converterParaDto(taxaAtualizada);
    }
    
    @Transactional
    public void desativarTaxa(String id) {
        Taxa taxa = taxaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa não encontrada"));
        taxa.setAtivo(false);
        taxaRepository.save(taxa);
    }
    
    @Transactional
    public void ativarTaxa(String id) {
        Taxa taxa = taxaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa não encontrada"));
        taxa.setAtivo(true);
        taxaRepository.save(taxa);
    }
    
    private TaxaResponseDto converterParaDto(Taxa taxa) {
        return TaxaResponseDto.builder()
                .id(taxa.getId())
                .descricao(taxa.getDescricao())
                .percentual(taxa.getPercentual())
                .valorFixo(taxa.getValorFixo())
                .ativo(taxa.isAtivo())
                .build();
    }
}
