package com.senai.conta_bancaria.domain.service;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.PagamentoStatus;
import com.senai.conta_bancaria.domain.exception.BoletoVencidoException;
import com.senai.conta_bancaria.domain.exception.PagamentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.senai.conta_bancaria.domain.exception.TaxaInvalidaException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class PagamentoDomainService {
    
    public void validarPagamento(Pagamento pagamento) {
        if (pagamento.getValorPago() == null || pagamento.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagamentoInvalidoException("O valor do pagamento deve ser maior que zero");
        }
        
        if (pagamento.getBoleto() == null || pagamento.getBoleto().isBlank()) {
            throw new PagamentoInvalidoException("O boleto deve ser informado");
        }
        
        if (pagamento.getConta() == null) {
            throw new PagamentoInvalidoException("A conta deve ser informada");
        }
    }
    
    public void validarTaxa(Taxa taxa) {
        if (taxa.getDescricao() == null || taxa.getDescricao().isBlank()) {
            throw new TaxaInvalidaException("A descrição da taxa deve ser informada");
        }
        
        if (taxa.getPercentual() == null || taxa.getPercentual().compareTo(BigDecimal.ZERO) < 0) {
            throw new TaxaInvalidaException("O percentual da taxa deve ser maior ou igual a zero");
        }
        
        if (taxa.getPercentual().compareTo(new BigDecimal("100")) > 0) {
            throw new TaxaInvalidaException("O percentual da taxa não pode ser maior que 100%");
        }
        
        if (taxa.getValorFixo() != null && taxa.getValorFixo().compareTo(BigDecimal.ZERO) < 0) {
            throw new TaxaInvalidaException("O valor fixo da taxa deve ser maior ou igual a zero");
        }
    }
    
    public BigDecimal calcularTotalTaxas(BigDecimal valorBase, Set<Taxa> taxas) {
        if (taxas == null || taxas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return taxas.stream()
                .map(taxa -> taxa.calcularTaxa(valorBase))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calcularValorTotal(BigDecimal valorPago, Set<Taxa> taxas) {
        BigDecimal totalTaxas = calcularTotalTaxas(valorPago, taxas);
        return valorPago.add(totalTaxas);
    }
    
    public void validarSaldo(Conta conta, BigDecimal valorTotal) {
        if (conta.getSaldo().compareTo(valorTotal) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar o pagamento com as taxas");
        }
    }
    
    public void validarBoleto(String boleto) {
        // Aqui poderia ter validação de formato, data de vencimento, etc.
        // Por simplicidade, apenas validamos se não está vazio
        if (boleto == null || boleto.isBlank()) {
            throw new BoletoVencidoException("Boleto inválido");
        }
        
        // Simulação: se o boleto contém "VENCIDO" no código, lança exceção
        if (boleto.toUpperCase().contains("VENCIDO")) {
            throw new BoletoVencidoException("O boleto está vencido");
        }
    }
    
    public Pagamento processarPagamento(Conta conta, String boleto, BigDecimal valorPago, Set<Taxa> taxas) {
        validarBoleto(boleto);
        
        BigDecimal valorTotalTaxas = calcularTotalTaxas(valorPago, taxas);
        BigDecimal valorTotal = calcularValorTotal(valorPago, taxas);
        
        validarSaldo(conta, valorTotal);
        
        // Debita o valor total da conta
        conta.sacar(valorTotal);
        
        // Cria o pagamento com status de sucesso
        Pagamento pagamento = Pagamento.builder()
                .conta(conta)
                .boleto(boleto)
                .valorPago(valorPago)
                .valorTotalTaxas(valorTotalTaxas)
                .valorTotal(valorTotal)
                .taxas(taxas)
                .dataPagamento(LocalDateTime.now())
                .status(PagamentoStatus.SUCESSO)
                .build();
        
        return pagamento;
    }
}
