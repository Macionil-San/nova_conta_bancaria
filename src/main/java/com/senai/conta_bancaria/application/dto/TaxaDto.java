package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxaDto {
    
    @NotBlank(message = "A descrição da taxa é obrigatória")
    @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
    private String descricao;
    
    @NotNull(message = "O percentual da taxa é obrigatório")
    @DecimalMin(value = "0.0", message = "O percentual deve ser maior ou igual a zero")
    @DecimalMax(value = "100.0", message = "O percentual não pode ser maior que 100")
    private BigDecimal percentual;
    
    @DecimalMin(value = "0.0", message = "O valor fixo deve ser maior ou igual a zero")
    private BigDecimal valorFixo;
}
