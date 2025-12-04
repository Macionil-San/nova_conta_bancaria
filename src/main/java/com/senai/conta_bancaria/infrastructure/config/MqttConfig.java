package com.senai.conta_bancaria.infrastructure.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {
    
    @Value("${mqtt.broker.url:tcp://localhost:1883}")
    private String brokerUrl;
    
    @Value("${mqtt.client.id:banco-backend}")
    private String clientId;
    
    @Value("${mqtt.username:}")
    private String username;
    
    @Value("${mqtt.password:}")
    private String password;
    
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        
        if (username != null && !username.isEmpty()) {
            options.setUserName(username);
        }
        
        if (password != null && !password.isEmpty()) {
            options.setPassword(password.toCharArray());
        }
        
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(60);
        
        factory.setConnectionOptions(options);
        return factory;
    }
    
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId + "-pub", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("banco/default");
        messageHandler.setConverter(new DefaultPahoMessageConverter());
        return messageHandler;
    }
}
