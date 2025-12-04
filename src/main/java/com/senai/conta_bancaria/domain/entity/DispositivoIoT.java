package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dispositivo_iot")
public class DispositivoIoT {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String codigoSerial;
    
    @Column(nullable = false, length = 500)
    private String chavePublica;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_dispositivo_cliente"))
    private Cliente cliente;
    
    @Column
    private LocalDateTime dataCadastro;
    
    @Column
    private LocalDateTime ultimoAcesso;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
    
    public void registrarAcesso() {
        this.ultimoAcesso = LocalDateTime.now();
    }
}
