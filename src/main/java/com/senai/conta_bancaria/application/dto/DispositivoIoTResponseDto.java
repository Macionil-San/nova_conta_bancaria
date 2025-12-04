package com.senai.conta_bancaria.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoIoTResponseDto {
    private String id;
    private String codigoSerial;
    private String chavePublica;
    private boolean ativo;
    private String clienteId;
    private String clienteNome;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimoAcesso;
}
