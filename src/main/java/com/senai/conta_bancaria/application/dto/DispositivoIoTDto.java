package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoIoTDto {
    
    @NotBlank(message = "O código serial é obrigatório")
    @Size(max = 100, message = "O código serial deve ter no máximo 100 caracteres")
    private String codigoSerial;
    
    @NotBlank(message = "A chave pública é obrigatória")
    @Size(max = 500, message = "A chave pública deve ter no máximo 500 caracteres")
    private String chavePublica;
    
    @NotBlank(message = "O ID do cliente é obrigatório")
    private String clienteId;
}
