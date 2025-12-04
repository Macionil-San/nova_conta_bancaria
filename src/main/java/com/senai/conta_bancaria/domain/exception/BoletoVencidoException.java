package com.senai.conta_bancaria.domain.exception;

public class BoletoVencidoException extends RuntimeException {
    public BoletoVencidoException(String mensagem) {
        super(mensagem);
    }
}
