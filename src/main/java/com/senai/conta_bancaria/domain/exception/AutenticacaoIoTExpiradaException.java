package com.senai.conta_bancaria.domain.exception;

public class AutenticacaoIoTExpiradaException extends RuntimeException {
    public AutenticacaoIoTExpiradaException(String mensagem) {
        super(mensagem);
    }
}
