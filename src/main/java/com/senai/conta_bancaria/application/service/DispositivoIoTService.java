package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.DispositivoIoTDto;
import com.senai.conta_bancaria.application.dto.DispositivoIoTResponseDto;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.DispositivoIoT;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.DispositivoIoTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DispositivoIoTService {
    
    private final DispositivoIoTRepository dispositivoRepository;
    private final ClienteRepository clienteRepository;
    
    @Transactional
    public DispositivoIoTResponseDto cadastrarDispositivo(DispositivoIoTDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
        
        if (dispositivoRepository.existsByClienteId(cliente.getId())) {
            throw new IllegalStateException("Cliente já possui um dispositivo IoT cadastrado");
        }
        
        DispositivoIoT dispositivo = DispositivoIoT.builder()
                .codigoSerial(dto.getCodigoSerial())
                .chavePublica(dto.getChavePublica())
                .cliente(cliente)
                .ativo(true)
                .build();
        
        DispositivoIoT dispositivoSalvo = dispositivoRepository.save(dispositivo);
        return converterParaDto(dispositivoSalvo);
    }
    
    @Transactional(readOnly = true)
    public List<DispositivoIoTResponseDto> listarTodos() {
        return dispositivoRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public DispositivoIoTResponseDto buscarPorId(String id) {
        DispositivoIoT dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Dispositivo não encontrado"));
        return converterParaDto(dispositivo);
    }
    
    @Transactional(readOnly = true)
    public DispositivoIoTResponseDto buscarPorCliente(String clienteId) {
        DispositivoIoT dispositivo = dispositivoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não possui dispositivo IoT"));
        return converterParaDto(dispositivo);
    }
    
    @Transactional
    public void desativarDispositivo(String id) {
        DispositivoIoT dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Dispositivo não encontrado"));
        dispositivo.setAtivo(false);
        dispositivoRepository.save(dispositivo);
    }
    
    @Transactional
    public void ativarDispositivo(String id) {
        DispositivoIoT dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Dispositivo não encontrado"));
        dispositivo.setAtivo(true);
        dispositivoRepository.save(dispositivo);
    }
    
    private DispositivoIoTResponseDto converterParaDto(DispositivoIoT dispositivo) {
        return DispositivoIoTResponseDto.builder()
                .id(dispositivo.getId())
                .codigoSerial(dispositivo.getCodigoSerial())
                .chavePublica(dispositivo.getChavePublica())
                .ativo(dispositivo.isAtivo())
                .clienteId(dispositivo.getCliente().getId())
                .clienteNome(dispositivo.getCliente().getNome())
                .dataCadastro(dispositivo.getDataCadastro())
                .ultimoAcesso(dispositivo.getUltimoAcesso())
                .build();
    }
}
