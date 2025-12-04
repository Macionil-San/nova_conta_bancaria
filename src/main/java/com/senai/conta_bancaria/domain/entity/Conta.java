package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.senai.conta_bancaria.domain.exception.TransferenciaParaMesmaContaException;
import com.senai.conta_bancaria.domain.exception.ValoresNegativosException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING, length = 8)
@Table(
        name = "conta",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_conta_numero", columnNames = "numero"),
                @UniqueConstraint(name = "uk_cliente_tipo", columnNames = {"cliente_id", "tipo_conta"})
        }
)
public abstract class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 20)
    private Long numero;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_conta_cliente"))
    private Cliente cliente;

    public abstract String getTipo();

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(getSaldo()) > 0)
            throw new SaldoInsuficienteException("saque");
        validarValorMaiorQueZero(valor, "saque");

        saldo = saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "depósito");

        saldo = saldo.add(valor);
    }

    public void transferir(Conta contaDestino, BigDecimal valor) {
        if (id.equals(contaDestino.getId()))
            throw new TransferenciaParaMesmaContaException();

        sacar(valor);
        contaDestino.depositar(valor);
    }

    protected void validarValorMaiorQueZero(BigDecimal valor, String operacao) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValoresNegativosException(operacao);
    }
}