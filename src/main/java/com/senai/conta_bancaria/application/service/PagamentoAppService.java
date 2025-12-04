package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.PagamentoDto;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDto;
import com.senai.conta_bancaria.application.dto.TaxaResponseDto;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.PagamentoStatus;
import com.senai.conta_bancaria.domain.exception.BoletoVencidoException;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import com.senai.conta_bancaria.domain.service.PagamentoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoAppService {
    
    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;
    private final PagamentoDomainService pagamentoDomainService;
    
    @Transactional
    public PagamentoResponseDto realizarPagamento(PagamentoDto dto) {
        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta não encontrada"));
        
        Set<Taxa> taxas = new HashSet<>();
        if (dto.getTaxasIds() != null && !dto.getTaxasIds().isEmpty()) {
            taxas = dto.getTaxasIds().stream()
                    .map(id -> taxaRepository.findById(id)
                            .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa não encontrada: " + id)))
                    .collect(Collectors.toSet());
        }
        
        Pagamento pagamento;
        try {
            pagamento = pagamentoDomainService.processarPagamento(
                    conta, 
                    dto.getBoleto(), 
                    dto.getValorPago(), 
                    taxas
            );
            pagamento.setObservacao(dto.getObservacao());
            
        } catch (SaldoInsuficienteException e) {
            pagamento = criarPagamentoFalha(conta, dto, taxas, PagamentoStatus.SALDO_INSUFICIENTE, e.getMessage());
        } catch (BoletoVencidoException e) {
            pagamento = criarPagamentoFalha(conta, dto, taxas, PagamentoStatus.BOLETO_VENCIDO, e.getMessage());
        } catch (Exception e) {
            pagamento = criarPagamentoFalha(conta, dto, taxas, PagamentoStatus.FALHA, e.getMessage());
        }
        
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);
        contaRepository.save(conta);
        
        return converterParaDto(pagamentoSalvo);
    }
    
    private Pagamento criarPagamentoFalha(Conta conta, PagamentoDto dto, Set<Taxa> taxas, 
                                          PagamentoStatus status, String observacao) {
        return Pagamento.builder()
                .conta(conta)
                .boleto(dto.getBoleto())
                .valorPago(dto.getValorPago())
                .valorTotalTaxas(pagamentoDomainService.calcularTotalTaxas(dto.getValorPago(), taxas))
                .valorTotal(pagamentoDomainService.calcularValorTotal(dto.getValorPago(), taxas))
                .taxas(taxas)
                .dataPagamento(LocalDateTime.now())
                .status(status)
                .observacao(observacao)
                .build();
    }
    
    @Transactional(readOnly = true)
    public List<PagamentoResponseDto> listarTodos() {
        return pagamentoRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PagamentoResponseDto> listarPorConta(String contaId) {
        return pagamentoRepository.findByContaId(contaId).stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PagamentoResponseDto> listarPorCliente(String clienteId) {
        return pagamentoRepository.findByContaClienteId(clienteId).stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PagamentoResponseDto buscarPorId(String id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pagamento não encontrado"));
        return converterParaDto(pagamento);
    }
    
    private PagamentoResponseDto converterParaDto(Pagamento pagamento) {
        Set<TaxaResponseDto> taxasDto = pagamento.getTaxas().stream()
                .map(taxa -> TaxaResponseDto.builder()
                        .id(taxa.getId())
                        .descricao(taxa.getDescricao())
                        .percentual(taxa.getPercentual())
                        .valorFixo(taxa.getValorFixo())
                        .ativo(taxa.isAtivo())
                        .build())
                .collect(Collectors.toSet());
        
        return PagamentoResponseDto.builder()
                .id(pagamento.getId())
                .contaId(pagamento.getConta().getId())
                .numeroConta(pagamento.getConta().getNumero().toString())
                .boleto(pagamento.getBoleto())
                .valorPago(pagamento.getValorPago())
                .valorTotalTaxas(pagamento.getValorTotalTaxas())
                .valorTotal(pagamento.getValorTotal())
                .dataPagamento(pagamento.getDataPagamento())
                .status(pagamento.getStatus())
                .taxas(taxasDto)
                .observacao(pagamento.getObservacao())
                .build();
    }
}
