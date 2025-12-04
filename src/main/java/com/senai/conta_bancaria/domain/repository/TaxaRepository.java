package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxaRepository extends JpaRepository<Taxa, String> {
    List<Taxa> findByAtivoTrue();
}
