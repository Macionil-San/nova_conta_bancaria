package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exception.TipoDeContaInvalidaException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContaResumoDto(
        @NotNull(message = "O número da conta não pode ser nulo.")
        @PositiveOrZero(message = "O número da conta não pode ser negativo.")
        Long numero,

        @NotNull(message = "O tipo da conta não pode ser nulo.")
        @NotBlank(message = "O tipo da conta não pode ser vazio.")
        @Pattern(regexp = "CORRENTE|POUPANCA")
        String tipo,

        @NotNull(message = "O saldo não pode ser nulo.")
        BigDecimal saldo
) {
    public static ContaResumoDto fromEntity(Conta conta) {
        return new ContaResumoDto(
                conta.getNumero(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }

    public Conta toEntity(Cliente cliente) {
        return switch (tipo) {
            case "CORRENTE" -> ContaCorrente.builder()
                    .id(null)
                    .numero(numero)
                    .saldo(saldo)
                    .ativo(true)
                    .cliente(cliente)
                    .limite(new BigDecimal("500.00"))
                    .taxa(new BigDecimal("0.05"))
                    .build();
            case "POUPANCA" -> ContaPoupanca.builder()
                    .id(null)
                    .numero(numero)
                    .saldo(saldo)
                    .ativo(true)
                    .cliente(cliente)
                    .rendimento(new BigDecimal("0.03"))
                    .build();
            default -> throw new TipoDeContaInvalidaException(tipo);
        };
    }
}