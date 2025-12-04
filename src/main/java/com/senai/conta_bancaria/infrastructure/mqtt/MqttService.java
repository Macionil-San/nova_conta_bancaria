package com.senai.conta_bancaria.infrastructure.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttService {
    
    private final MessageChannel mqttOutboundChannel;
    
    public void publicar(String topico, String mensagem) {
        try {
            Message<String> message = MessageBuilder
                    .withPayload(mensagem)
                    .setHeader(MqttHeaders.TOPIC, topico)
                    .setHeader(MqttHeaders.QOS, 1)
                    .build();
            
            mqttOutboundChannel.send(message);
            log.info("Mensagem publicada no tópico {}: {}", topico, mensagem);
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem MQTT no tópico {}: {}", topico, e.getMessage());
            throw new RuntimeException("Erro ao publicar mensagem MQTT", e);
        }
    }
    
    public void solicitarAutenticacao(String clienteId, String codigoEsperado) {
        String topico = "banco/autenticacao/" + clienteId;
        String mensagem = String.format("{\"clienteId\":\"%s\",\"codigo\":\"%s\"}", clienteId, codigoEsperado);
        publicar(topico, mensagem);
    }
}
