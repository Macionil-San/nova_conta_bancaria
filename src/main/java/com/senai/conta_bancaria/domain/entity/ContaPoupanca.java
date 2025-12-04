package com.senai.conta_bancaria.domain.entity;

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
@DiscriminatorValue("POUPANCA")
public class ContaPoupanca extends Conta {
    @Column(precision = 19, scale = 2)
    private BigDecimal rendimento;

    @Override
    public String getTipo() {
        return "POUPANCA";
    }

    public void aplicarRendimento() {
        BigDecimal valorRendimento = getSaldo().multiply(rendimento);
        setSaldo(getSaldo().add(valorRendimento));
    }
}