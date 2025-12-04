package com.senai.conta_bancaria.domain.exception;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String operacao) {
        super("Saldo insuficiente para realizar " + operacao);
    }
}