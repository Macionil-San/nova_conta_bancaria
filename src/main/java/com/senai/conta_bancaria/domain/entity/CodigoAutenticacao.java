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
@Table(name = "codigo_autenticacao")
public class CodigoAutenticacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 10)
    private String codigo;
    
    @Column(nullable = false)
    private LocalDateTime expiraEm;
    
    @Column(nullable = false)
    private boolean validado = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_codigo_cliente"))
    private Cliente cliente;
    
    @Column(nullable = false)
    private LocalDateTime criadoEm;
    
    @Column
    private LocalDateTime validadoEm;
    
    @Column(length = 50)
    private String tipoOperacao;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
    
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(expiraEm);
    }
    
    public void validar() {
        this.validado = true;
        this.validadoEm = LocalDateTime.now();
    }
}
