package com.senai.conta_bancaria.domain.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade) {
        super("Entidade " + entidade + " não encontrada.");
    }
}