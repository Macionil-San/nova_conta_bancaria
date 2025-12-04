package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.enums.PagamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
    List<Pagamento> findByContaId(String contaId);
    List<Pagamento> findByStatus(PagamentoStatus status);
    List<Pagamento> findByContaClienteId(String clienteId);
}
