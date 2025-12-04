package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.enums.PagamentoStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pagamento")
public class Pagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private Conta conta;
    
    @Column(nullable = false, length = 120)
    private String boleto;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago;
    
    @Column(nullable = false)
    private LocalDateTime dataPagamento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PagamentoStatus status;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "pagamento_taxa",
        joinColumns = @JoinColumn(name = "pagamento_id", foreignKey = @ForeignKey(name = "fk_pagamento_taxa_pagamento")),
        inverseJoinColumns = @JoinColumn(name = "taxa_id", foreignKey = @ForeignKey(name = "fk_pagamento_taxa_taxa"))
    )
    private Set<Taxa> taxas = new HashSet<>();
    
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotalTaxas;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(length = 500)
    private String observacao;
}
