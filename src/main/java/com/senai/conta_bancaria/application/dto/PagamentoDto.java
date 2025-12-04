package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDto {
    
    @NotBlank(message = "O ID da conta é obrigatório")
    private String contaId;
    
    @NotBlank(message = "O código do boleto é obrigatório")
    @Size(max = 120, message = "O código do boleto deve ter no máximo 120 caracteres")
    private String boleto;
    
    @NotNull(message = "O valor do pagamento é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valorPago;
    
    private Set<String> taxasIds;
    
    private String observacao;
}
