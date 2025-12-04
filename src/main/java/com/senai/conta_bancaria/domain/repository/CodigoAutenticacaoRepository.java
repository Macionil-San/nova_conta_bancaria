package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodigoAutenticacaoRepository extends JpaRepository<CodigoAutenticacao, String> {
    Optional<CodigoAutenticacao> findByCodigoAndClienteIdAndValidadoFalse(String codigo, String clienteId);
    Optional<CodigoAutenticacao> findTopByClienteIdAndValidadoFalseOrderByExpiraEmDesc(String clienteId);
}
