package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ValorSaqueDepositoDto(
        @NotNull(message = "O valor não pode ser nulo.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor
) {
}