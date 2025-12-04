package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends Conta {
    @Column(precision = 19, scale = 2)
    private BigDecimal limite;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxa;

    @Override
    public String getTipo() {
        return "CORRENTE";
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "saque");

        BigDecimal custoTaxa = valor.multiply(taxa);
        BigDecimal valorComTaxa = valor.add(custoTaxa);

        if (valorComTaxa.compareTo(getSaldo().add(limite)) > 0)
            throw new SaldoInsuficienteException("saque");

        setSaldo(getSaldo().subtract(valorComTaxa));
    }
}