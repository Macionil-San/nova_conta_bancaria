package com.senai.conta_bancaria.domain.exception;

public class ValoresNegativosException extends RuntimeException {
    public ValoresNegativosException(String operacao) {
        super("O valor de " + operacao + " deve ser maior que zero.");
    }
}