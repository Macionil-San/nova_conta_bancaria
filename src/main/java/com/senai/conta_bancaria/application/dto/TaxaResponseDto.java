package com.senai.conta_bancaria.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxaResponseDto {
    private String id;
    private String descricao;
    private BigDecimal percentual;
    private BigDecimal valorFixo;
    private boolean ativo;
}
