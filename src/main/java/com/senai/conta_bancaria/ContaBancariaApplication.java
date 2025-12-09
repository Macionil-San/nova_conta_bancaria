package com.senai.conta_bancaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
                                                                                //MQTT não está funcinando corretamente
                                                                                // dependendo da maquina ( not ta indo)
@SpringBootApplication
public class ContaBancariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContaBancariaApplication.class, args);
    }
}