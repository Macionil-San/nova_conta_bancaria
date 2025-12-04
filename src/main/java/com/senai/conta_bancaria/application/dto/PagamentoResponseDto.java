package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.enums.PagamentoStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponseDto {
    private String id;
    private String contaId;
    private String numeroConta;
    private String boleto;
    private BigDecimal valorPago;
    private BigDecimal valorTotalTaxas;
    private BigDecimal valorTotal;
    private LocalDateTime dataPagamento;
    private PagamentoStatus status;
    private Set<TaxaResponseDto> taxas;
    private String observacao;
}
