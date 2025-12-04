package com.senai.conta_bancaria.domain.exception;

public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException(String tipo) {
        super("Tipo de conta " + tipo + " inválida. Tipos válidos: 'CORRENTE', 'POUPANCA'.");
    }
}