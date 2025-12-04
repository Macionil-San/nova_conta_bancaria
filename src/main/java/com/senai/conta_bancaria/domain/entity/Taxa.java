package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taxa")
public class Taxa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 100)
    private String descricao;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentual;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal valorFixo;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @ManyToMany(mappedBy = "taxas")
    private Set<Pagamento> pagamentos = new HashSet<>();
    
    public BigDecimal calcularTaxa(BigDecimal valorBase) {
        BigDecimal taxaPercentual = valorBase.multiply(percentual).divide(new BigDecimal("100"));
        BigDecimal taxaFixa = valorFixo != null ? valorFixo : BigDecimal.ZERO;
        return taxaPercentual.add(taxaFixa);
    }
}
