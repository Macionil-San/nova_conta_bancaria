package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, String> {
    Optional<Conta> findByNumeroAndAtivoTrue(Long cpf);

    List<Conta> findAllByAtivoTrue();
}