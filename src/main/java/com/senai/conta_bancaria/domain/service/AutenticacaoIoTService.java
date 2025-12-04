package com.senai.conta_bancaria.domain.service;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.senai.conta_bancaria.domain.entity.DispositivoIoT;
import com.senai.conta_bancaria.domain.exception.AutenticacaoIoTExpiradaException;
import com.senai.conta_bancaria.domain.exception.DispositivoIoTInativoException;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import com.senai.conta_bancaria.domain.repository.DispositivoIoTRepository;
import com.senai.conta_bancaria.infrastructure.mqtt.MqttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoIoTService {
    
    private final DispositivoIoTRepository dispositivoRepository;
    private final CodigoAutenticacaoRepository codigoRepository;
    private final MqttService mqttService;
    
    private static final int TEMPO_EXPIRACAO_MINUTOS = 2;
    private static final SecureRandom random = new SecureRandom();
    
    @Transactional
    public String iniciarAutenticacao(Cliente cliente, String tipoOperacao) {
        DispositivoIoT dispositivo = dispositivoRepository.findByClienteId(cliente.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Cliente não possui dispositivo IoT cadastrado"));
        
        if (!dispositivo.isAtivo()) {
            throw new DispositivoIoTInativoException(
                    "O dispositivo IoT do cliente está inativo");
        }
        
        // Gera código aleatório de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(1000000));
        
        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(TEMPO_EXPIRACAO_MINUTOS);
        
        CodigoAutenticacao codigoAutenticacao = CodigoAutenticacao.builder()
                .codigo(codigo)
                .cliente(cliente)
                .expiraEm(expiraEm)
                .validado(false)
                .tipoOperacao(tipoOperacao)
                .build();
        
        codigoRepository.save(codigoAutenticacao);
        
        // Publica mensagem MQTT solicitando autenticação biométrica
        mqttService.solicitarAutenticacao(cliente.getId(), codigo);
        
        dispositivo.registrarAcesso();
        dispositivoRepository.save(dispositivo);
        
        log.info("Autenticação IoT iniciada para cliente {} - Operação: {}", 
                cliente.getId(), tipoOperacao);
        
        return codigoAutenticacao.getId();
    }
    
    @Transactional
    public boolean validarCodigo(String clienteId, String codigo) {
        CodigoAutenticacao codigoAutenticacao = codigoRepository
                .findByCodigoAndClienteIdAndValidadoFalse(codigo, clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Código de autenticação não encontrado ou já validado"));
        
        if (codigoAutenticacao.isExpirado()) {
            throw new AutenticacaoIoTExpiradaException(
                    "O código de autenticação expirou. Tempo limite: " + TEMPO_EXPIRACAO_MINUTOS + " minutos");
        }
        
        codigoAutenticacao.validar();
        codigoRepository.save(codigoAutenticacao);
        
        log.info("Código de autenticação validado com sucesso para cliente {}", clienteId);
        
        return true;
    }
    
    @Transactional(readOnly = true)
    public boolean verificarAutenticacaoPendente(String clienteId) {
        return codigoRepository
                .findTopByClienteIdAndValidadoFalseOrderByExpiraEmDesc(clienteId)
                .map(codigo -> !codigo.isExpirado())
                .orElse(false);
    }
    
    @Transactional(readOnly = true)
    public boolean clientePossuiDispositivoAtivo(String clienteId) {
        return dispositivoRepository.findByClienteId(clienteId)
                .map(DispositivoIoT::isAtivo)
                .orElse(false);
    }
}
