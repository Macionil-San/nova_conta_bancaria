package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TransferenciaDto(
        @NotNull(message = "O número da conta destinatária não pode ser nulo.")
        @PositiveOrZero(message = "O número da conta destinatária não pode ser negativo.")
        Long numeroDestino,

        @NotNull(message = "O valor transferido não pode ser nulo.")
        @Positive(message = "O valor transferido deve ser maior que zero.")
        BigDecimal valor
) {
}