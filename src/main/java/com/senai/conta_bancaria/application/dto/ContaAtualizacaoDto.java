package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContaAtualizacaoDto(
        @NotNull(message = "O saldo não pode ser nulo.")
        BigDecimal saldo,

        @NotNull(message = "O limite não pode ser nulo.")
        @PositiveOrZero(message = "O limite não pode ser negativo.")
        BigDecimal limite,

        @NotNull(message = "A taxa não pode ser nulo.")
        @PositiveOrZero(message = "A taxa não pode ser negativa.")
        BigDecimal taxa,

        @NotNull(message = "O rendimento não pode ser nulo.")
        @PositiveOrZero(message = "O rendimento não pode ser negativo.")
        BigDecimal rendimento
) {
}