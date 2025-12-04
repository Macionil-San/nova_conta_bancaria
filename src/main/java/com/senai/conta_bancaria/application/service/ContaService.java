package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ContaResumoDto;
import com.senai.conta_bancaria.application.dto.TransferenciaDto;
import com.senai.conta_bancaria.application.dto.ValorSaqueDepositoDto;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.exception.RendimentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.TipoDeContaInvalidaException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {
    private final ContaRepository repository;

    // CREATE: embutido em Cliente

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ContaResumoDto> listarTodasAsContas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(ContaResumoDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ContaResumoDto> listarContasPorCpf(Long cpf) {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .filter(c -> c.getCliente().getCpf().equals(cpf))
                .map(ContaResumoDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto buscarConta(Long numero) {
        return ContaResumoDto.fromEntity(procurarContaAtiva(numero));
    }

    // UPDATE
    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto atualizarConta(Long numero, ContaAtualizacaoDto dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.setSaldo(dto.saldo());
        if (conta instanceof ContaCorrente contaCorrente) {
            contaCorrente.setLimite(dto.limite());
            contaCorrente.setTaxa(dto.taxa());
        } else if (conta instanceof ContaPoupanca contaPoupanca) {
            contaPoupanca.setRendimento(dto.rendimento());
        } else {
            throw new TipoDeContaInvalidaException("");
        }

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    // DELETE
    @PreAuthorize("hasRole('CLIENTE')")
    public void apagarConta(Long numero) {
        Conta conta = procurarContaAtiva(numero);

        conta.setAtivo(false);

        repository.save(conta);
    }

    // Ações específicas

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto sacar(Long numero, ValorSaqueDepositoDto dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.sacar(dto.valor());

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto depositar(Long numero, ValorSaqueDepositoDto dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.depositar(dto.valor());

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto transferir(Long numeroOrigem, TransferenciaDto dto) {
        Conta contaOrigem = procurarContaAtiva(numeroOrigem);
        Conta contaDestino = procurarContaAtiva(dto.numeroDestino());

        contaOrigem.transferir(contaDestino, dto.valor());

        repository.save(contaDestino);
        return ContaResumoDto.fromEntity(repository.save(contaOrigem));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDto rendimento(Long numero) {
        Conta conta = procurarContaAtiva(numero);

        if (!(conta instanceof ContaPoupanca contaPoupanca))
            throw new RendimentoInvalidoException();

        contaPoupanca.aplicarRendimento();

        return ContaResumoDto.fromEntity(repository.save(conta));
    }

    // Mét0do auxiliar para as requisições
    private Conta procurarContaAtiva(Long numero) {
        return repository
                .findByNumeroAndAtivoTrue(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("conta"));
    }
}