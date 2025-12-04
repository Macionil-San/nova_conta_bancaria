package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.AuthDto;
import com.senai.conta_bancaria.domain.entity.Usuario;
import com.senai.conta_bancaria.domain.exception.UsuarioNaoEncontradoException;
import com.senai.conta_bancaria.domain.repository.UsuarioRepository;
import com.senai.conta_bancaria.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public String login(AuthDto.LoginRequest req) {
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        if (!encoder.matches(req.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}