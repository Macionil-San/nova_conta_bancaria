package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoCodigoDto {
    
    @NotBlank(message = "O código é obrigatório")
    @Size(min = 6, max = 10, message = "O código deve ter entre 6 e 10 caracteres")
    private String codigo;
    
    @NotBlank(message = "O ID do cliente é obrigatório")
    private String clienteId;
}
