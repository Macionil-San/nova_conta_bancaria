package com.senai.conta_bancaria.domain.exception;

public class RendimentoInvalidoException extends RuntimeException {
    public RendimentoInvalidoException() {
        super("Rendimento deve ser apenas aplicado a contas poupança.");
    }
}