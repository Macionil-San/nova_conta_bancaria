package com.senai.conta_bancaria.domain.enums;

public enum PagamentoStatus {
    SUCESSO,
    FALHA,
    SALDO_INSUFICIENTE,
    BOLETO_VENCIDO,
    AGUARDANDO_AUTENTICACAO,
    AUTENTICACAO_EXPIRADA
}
