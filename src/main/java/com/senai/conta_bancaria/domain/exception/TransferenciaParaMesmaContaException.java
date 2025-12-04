package com.senai.conta_bancaria.domain.exception;

public class TransferenciaParaMesmaContaException extends RuntimeException {
    public TransferenciaParaMesmaContaException() {
        super("Não é possível transferir para a mesma conta.");
    }
}