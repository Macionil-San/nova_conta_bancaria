package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Gerente;
import jakarta.validation.constraints.*;

import java.util.List;

public record GerenteResponseDto(
        String id,

        @NotNull(message = "O nome não pode ser nulo.")
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        @NotNull(message = "O CPF não pode ser nulo.")
        @Positive(message = "O CPF não pode ser negativo.")
        @Max(value = 99999999999L, message = "O CPF deve ter até 11 digitos.")
        Long cpf,

        @Email
        @NotNull(message = "O e-mail não pode ser nulo.")
        @NotBlank(message = "O e-mail não pode ser vazio.")
        String email,

        @NotNull(message = "A senha não pode ser nula.")
        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres.")
        String senha
) {
    public static GerenteResponseDto fromEntity(Gerente gerente) {
        return new GerenteResponseDto(
                gerente.getId(),
                gerente.getNome(),
                gerente.getCpf(),
                gerente.getEmail(),
                gerente.getSenha()
        );
    }
}